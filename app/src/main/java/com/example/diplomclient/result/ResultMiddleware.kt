package com.example.diplomclient.result

import com.example.diplomclient.arch.flux.dispatcher.Dispatchable
import com.example.diplomclient.arch.flux.store.Middleware
import com.example.diplomclient.arch.flux.Action

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
