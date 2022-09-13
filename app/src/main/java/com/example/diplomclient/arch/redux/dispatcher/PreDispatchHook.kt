package com.example.diplomclient.arch.redux.dispatcher

import com.example.diplomclient.arch.redux.Action

fun interface PreDispatchHook {
    fun shouldDispatchAction(dispatchable: Dispatchable, action: Action): Boolean
}
