package com.example.diplomclient.result

import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
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
