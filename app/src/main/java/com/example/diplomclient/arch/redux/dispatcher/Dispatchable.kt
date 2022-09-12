package com.example.diplomclient.arch.redux.dispatcher

import com.example.diplomclient.arch.redux.Action

fun interface Dispatchable {
    fun dispatch(action: Action)
}
