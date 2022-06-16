package com.example.diplomclient.arch.flux.dispatcher

import com.example.diplomclient.arch.flux.Action

fun interface Dispatchable {
    fun dispatch(action: Action)
}
