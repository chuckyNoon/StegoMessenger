package com.example.stegomessenger.arch.redux.dispatcher

typealias NewStateListener<T> = (newState: T) -> Unit
