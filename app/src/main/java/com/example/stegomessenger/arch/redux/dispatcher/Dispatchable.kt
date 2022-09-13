package com.example.stegomessenger.arch.redux.dispatcher

import com.example.stegomessenger.arch.redux.Action

fun interface Dispatchable {
    fun dispatch(action: Action)
}
