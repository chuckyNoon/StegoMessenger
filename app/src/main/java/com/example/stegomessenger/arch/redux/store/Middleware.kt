package com.example.stegomessenger.arch.redux.store

import androidx.annotation.CallSuper
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class Middleware<T : Any> {

    protected val middlewareScope = CoroutineScope(SupervisorJob() +  Dispatchers.Default)
    @CallSuper
    open fun onDetached(dispatchable: Dispatchable){
        middlewareScope.cancel()
    }
    abstract fun onReduced(dispatchable: Dispatchable, action: Action, oldState: T, newState: T)
}
