package com.aita.arch.dispatcher

import com.example.diplomclient.arch.flux.Action

fun interface PreDispatchHook {

    fun onDetached(dispatchable: Dispatchable): Unit = Unit

    fun shouldDispatchAction(dispatchable: Dispatchable, action: Action): Boolean
}
