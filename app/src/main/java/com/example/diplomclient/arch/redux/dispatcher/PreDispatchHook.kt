package com.example.diplomclient.arch.redux.dispatcher

import com.example.diplomclient.arch.redux.Action

fun interface PreDispatchHook {

    fun onDetached(dispatchable: Dispatchable): Unit = Unit

    fun shouldDispatchAction(dispatchable: Dispatchable, action: Action): Boolean
}
