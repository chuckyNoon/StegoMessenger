package com.example.stegomessenger.arch.redux.dispatcher

import com.example.stegomessenger.arch.redux.Action

fun interface PreDispatchHook {
    fun shouldDispatchAction(dispatchable: Dispatchable, action: Action): Boolean
}
