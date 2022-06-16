package com.example.diplomclient.arch.flux.dispatcher

import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.ErrorListener
import com.example.diplomclient.arch.flux.disposable.Disposable
import com.example.diplomclient.arch.flux.disposable.ManagedDisposable
import com.example.diplomclient.arch.flux.store.Store
import java.util.concurrent.ExecutorService
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicBoolean

class Dispatcher internal constructor(
    private val workerExecutorService: ExecutorService,
    private val listenerExecutorService: ExecutorService,
) : Dispatchable {

    private val subscriptions = mutableListOf<Subscription<*>>()
    private val hookHolders = mutableListOf<HookHolder>()
    private val unconsumedActions = mutableListOf<Action>()

    private val isShutdownAtomic = AtomicBoolean(false)

    private val onStoreError: ErrorListener = this::notifyError

    private var onError: ErrorListener? = null

    @CheckResult
    fun <T : Any> attachStore(store: Store<T>, onNewState: NewStateListener<T>): Disposable {
        val subscription = Subscription(
            storeHolder = StoreHolder(
                store = store,
                disposableDispatchable = DisposableDispatchable(this),
            ),
            onNewState = onNewState,
        )
        synchronized(SUBSCRIPTIONS_LOCK) { subscriptions.add(subscription) }

        workerExecutorService.executeSafe {
            val dispatchResult = try {
                dispatchUnconsumedAcceptableActions(subscription)
            } catch (t: Throwable) {
                notifyError(t)
                return@executeSafe
            }

            listenerExecutorService.executeSafe {
                try {
                    dispatchResult.deliver()
                } catch (t: Throwable) {
                    notifyError(t)
                }
            }
        }

        val disposable = Disposable {
            synchronized(SUBSCRIPTIONS_LOCK) { subscriptions.remove(subscription) }
            subscription.onDetached(onStoreError)
        }
        return ManagedDisposable(disposable)
    }

    @CheckResult
    fun attachPreDispatchHook(hook: PreDispatchHook): Disposable {
        val hookHolder = HookHolder(hook, DisposableDispatchable(this))
        synchronized(HOOKS_LOCK) { hookHolders.add(hookHolder) }

        val disposable = Disposable {
            synchronized(HOOKS_LOCK) { hookHolders.remove(hookHolder) }
            try {
                hookHolder.onDetached()
            } catch (t: Throwable) {
                notifyError(t)
            }
        }
        return ManagedDisposable(disposable)
    }

    @CheckResult
    fun attachErrorListener(onError: ErrorListener): Disposable {
        this.onError = onError

        val disposable = Disposable {
            this@Dispatcher.onError = null
        }
        return ManagedDisposable(disposable)
    }

    override fun dispatch(action: Action): Unit =
        workerExecutorService.executeSafe {
            val dispatchResults = try {
                if (hooksPreventActionDispatch(action)) {
                    return@executeSafe
                }
                performDispatch(action)
            } catch (t: Throwable) {
                notifyError(t)
                return@executeSafe
            }

            if (dispatchResults.isEmpty()) {
                synchronized(UNCONSUMED_ACTIONS_LOCK) { unconsumedActions.add(action) }
                return@executeSafe
            }

            listenerExecutorService.executeSafe {
                try {
                    notifyNewState(dispatchResults)
                } catch (t: Throwable) {
                    notifyError(t)
                }
            }
        }

    private fun hooksPreventActionDispatch(action: Action): Boolean {
        synchronized(HOOKS_LOCK) {
            for (hookHolder in hookHolders) {
                val shouldDispatchAction = try {
                    hookHolder.shouldDispatchAction(action)
                } catch (t: Throwable) {
                    notifyError(t)
                    true
                }
                if (shouldDispatchAction) {
                    continue
                }
                return true
            }
        }
        return false
    }

    @WorkerThread
    private fun performDispatch(action: Action): List<DispatchResult<*>> {
        val matchingSubscriptions = synchronized(SUBSCRIPTIONS_LOCK) {
            subscriptions.filter { subscription -> subscription.acceptsAction(action) }
        }
        return matchingSubscriptions.mapNotNull { subscription ->
            try {
                subscription.dispatch(action, onStoreError)
            } catch (t: Throwable) {
                notifyError(t)
                null
            }
        }
    }

    private fun notifyNewState(dispatchResults: List<DispatchResult<*>>) {
        for (dispatchResult in dispatchResults) {
            dispatchResult.deliver()
        }
    }

    private fun notifyError(t: Throwable) =
        listenerExecutorService.executeSafe {
            val onError = onError
            if (onError == null) {
                t.printStackTrace()
            } else {
                onError(t)
            }
        }

    @WorkerThread
    private fun <T : Any> dispatchUnconsumedAcceptableActions(subscription: Subscription<T>): DispatchResult<T> {
        var newDispatchResult = subscription.toDispatchResult()
        synchronized(UNCONSUMED_ACTIONS_LOCK) {
            val unconsumedActionsIterator = unconsumedActions.iterator()
            while (unconsumedActionsIterator.hasNext()) {
                val unconsumedAction = unconsumedActionsIterator.next()
                if (!subscription.acceptsAction(unconsumedAction)) {
                    continue
                }
                newDispatchResult = try {
                    subscription.dispatch(unconsumedAction, onStoreError)
                } catch (t: Throwable) {
                    notifyError(t)
                    newDispatchResult
                }
                unconsumedActionsIterator.remove()
            }
        }
        /*
         * Explicitly saying that oldState != newState because
         * NewStateListener should always be notified on Store attach
         */
        return newDispatchResult.setIsNewStateSameAsOld(false)
    }

    fun shutdown(): Unit =
        runSynchronizedIfNotShutdown {
            isShutdownAtomic.set(true)
            workerExecutorService.shutdown()
            listenerExecutorService.shutdown()
        }

    private fun runSynchronizedIfNotShutdown(block: () -> Unit) {
        if (isShutdownAtomic.get()) {
            return
        }
        synchronized(IS_SHUTDOWN_LOCK) {
            if (isShutdownAtomic.get()) {
                return
            }
            block()
        }
    }

    private fun ExecutorService.executeSafe(runnable: Runnable) =
        runSynchronizedIfNotShutdown {
            try {
                execute(runnable)
            } catch (e: RejectedExecutionException) {
                // The best we can do in this case for now
                System.err.println("Dispatcher: ExecutorService.executeSafe caught something: $e")
                e.printStackTrace()
            }
        }

    companion object {
        private val SUBSCRIPTIONS_LOCK = Any()
        private val HOOKS_LOCK = Any()
        private val UNCONSUMED_ACTIONS_LOCK = Any()
        private val IS_SHUTDOWN_LOCK = Any()
    }
}

private class Subscription<T : Any>(
    private val storeHolder: StoreHolder<T>,
    private val onNewState: NewStateListener<T>,
) {

    @CheckResult
    fun acceptsAction(action: Action): Boolean =
        storeHolder.acceptsAction(action)

    fun notifyNewState(newState: T) =
        onNewState(newState)

    @WorkerThread
    fun dispatch(action: Action, onStoreError: ErrorListener): DispatchResult<T> {
        val oldState = storeHolder.state
        val newState = storeHolder.dispatch(action, onStoreError)
        return DispatchResult(
            subscription = this,
            newState = newState,
            isNewStateSameAsOld = oldState == newState,
        )
    }

    fun toDispatchResult(): DispatchResult<T> =
        DispatchResult(
            subscription = this,
            newState = storeHolder.state,
            isNewStateSameAsOld = false,
        )

    fun onDetached(onStoreError: ErrorListener) =
        storeHolder.onDetached(onStoreError)
}

private class DispatchResult<T : Any>(
    private val subscription: Subscription<T>,
    private val newState: T,
    private val isNewStateSameAsOld: Boolean,
) {

    fun deliver() {
        if (isNewStateSameAsOld) {
            return
        }
        subscription.notifyNewState(newState)
    }

    fun setIsNewStateSameAsOld(newIsNewStateSameAsOld: Boolean): DispatchResult<T> =
        if (this.isNewStateSameAsOld == newIsNewStateSameAsOld) {
            this
        } else {
            DispatchResult(
                subscription = this.subscription,
                newState = this.newState,
                isNewStateSameAsOld = newIsNewStateSameAsOld,
            )
        }
}

private class HookHolder(
    private val hook: PreDispatchHook,
    private val disposableDispatchable: DisposableDispatchable,
) {

    fun onDetached() {
        hook.onDetached(disposableDispatchable)
        disposableDispatchable.dispose()
    }

    fun shouldDispatchAction(action: Action): Boolean =
        hook.shouldDispatchAction(disposableDispatchable, action)
}

private class StoreHolder<T : Any>(
    private val store: Store<T>,
    private val disposableDispatchable: DisposableDispatchable,
) {

    val state: T
        get() = store.state

    fun acceptsAction(action: Action): Boolean =
        store.acceptsAction(action)

    fun dispatch(action: Action, onError: ErrorListener): T =
        store.dispatch(action, disposableDispatchable, onError)

    fun onDetached(onError: ErrorListener) {
        store.onDetached(disposableDispatchable, onError)
        disposableDispatchable.dispose()
    }
}
