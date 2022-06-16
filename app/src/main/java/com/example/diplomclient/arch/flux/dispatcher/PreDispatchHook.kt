package com.example.diplomclient.arch.flux.dispatcher

import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.flux.dispatcher.Dispatchable

fun interface PreDispatchHook {

    fun onDetached(dispatchable: Dispatchable): Unit = Unit

    fun shouldDispatchAction(dispatchable: Dispatchable, action: Action): Boolean
}
