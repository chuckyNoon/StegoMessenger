package com.example.diplomclient.arch.infra

import android.app.Application
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import com.example.diplomclient.arch.util.AppDepsProvider
import com.example.diplomclient.arch.redux.dispatcher.Dispatchable
import com.example.diplomclient.arch.redux.dispatcher.NewStateListener
import com.example.diplomclient.arch.redux.dispatcher.PreDispatchHook
import com.example.diplomclient.arch.redux.disposable.Disposable
import com.example.diplomclient.arch.redux.store.Middleware
import com.example.diplomclient.arch.redux.store.Reducer
import com.example.diplomclient.arch.redux.store.Store

abstract class AbsViewModel(
    app: Application,
    private val appDepsProvider: AppDepsProvider,
) :
    AndroidViewModel(app),
    Dispatchable by appDepsProvider.dispatcher {

    private val disposables = mutableListOf<Disposable>()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        for (disposable in disposables) {
            disposable.dispose()
        }
    }

    @MainThread
    protected fun <T : Any> attachManagedStore(
        initialState: T,
        reducer: Reducer<T>,
        middleware: List<Middleware<T>> = emptyList(),
        newStateListener: NewStateListener<T>,
    ): Unit = keepTrackOf(
        disposable = appDepsProvider.dispatcher.attachStore(
            Store(initialState, reducer, middleware),
            newStateListener,
        )
    )

    @MainThread
    protected fun attachManagedPreDispatchHook(hook: PreDispatchHook): Unit = keepTrackOf(
        disposable = appDepsProvider.dispatcher.attachPreDispatchHook(hook)
    )

    @MainThread
    protected fun keepTrackOf(disposable: Disposable) {
        disposables.add(disposable)
    }
}
