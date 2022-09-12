package com.example.diplomclient.result

import com.example.diplomclient.arch.redux.dispatcher.Dispatchable
import com.example.diplomclient.arch.redux.store.Middleware
import com.example.diplomclient.arch.redux.Action

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
