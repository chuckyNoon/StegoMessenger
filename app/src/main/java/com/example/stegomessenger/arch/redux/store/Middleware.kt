package com.example.stegomessenger.arch.redux.store

import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable

fun interface Middleware<T : Any> {

    fun onDetached(dispatchable: Dispatchable): Unit = Unit

    fun onReduced(dispatchable: Dispatchable, action: Action, oldState: T, newState: T)
}
