package com.aita.arch.store

import com.example.diplomclient.arch.flux.Action
import com.aita.arch.dispatcher.Dispatchable

fun interface Middleware<T : Any> {

    fun onDetached(dispatchable: Dispatchable): Unit = Unit

    fun onReduced(dispatchable: Dispatchable, action: Action, oldState: T, newState: T)
}
