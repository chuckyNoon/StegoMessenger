package com.example.diplomclient.arch.redux.store

import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.arch.redux.dispatcher.Dispatchable

fun interface Middleware<T : Any> {

    fun onDetached(dispatchable: Dispatchable): Unit = Unit

    fun onReduced(dispatchable: Dispatchable, action: Action, oldState: T, newState: T)
}
