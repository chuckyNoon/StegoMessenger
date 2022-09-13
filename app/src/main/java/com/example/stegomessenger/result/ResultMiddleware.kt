package com.example.stegomessenger.result

import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action

class ResultMiddleware : Middleware<ResultState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: ResultState,
        newState: ResultState
    ) {
        when (action) {
            else -> {
            }
        }
    }
}
