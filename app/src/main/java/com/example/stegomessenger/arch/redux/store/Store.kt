package com.example.stegomessenger.arch.redux.store

import androidx.annotation.CheckResult
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.ErrorListener
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.common.AppLogger

class Store<T : Any>(
    initialState: T,
    private val reducer: Reducer<T>,
    private val middleware: List<Middleware<T>> = emptyList(),
) {

    var state: T = initialState
        private set

    @CheckResult
    fun acceptsAction(action: Action): Boolean =
        reducer.acceptsAction(action)

    @CheckResult
    fun dispatch(
        action: Action,
        dispatchable: Dispatchable,
        onError: ErrorListener,
    ): T {
        AppLogger.log(action.logMsg)
        val oldState = state
        val newState = try {
            reducer.reduce(oldState, action)
        } catch (t: Throwable) {
            onError(t)
            oldState
        }
        middleware.forEach {
            try {
                it.onReduced(dispatchable, action, oldState, newState)
            } catch (t: Throwable) {
                onError(t)
            }
        }
        this.state = newState
        return newState
    }

    internal fun onDetached(
        dispatchable: Dispatchable,
        onError: ErrorListener,
    ) {
        middleware.forEach {
            try {
                it.onDetached(dispatchable)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}
