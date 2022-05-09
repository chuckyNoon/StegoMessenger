package com.example.diplomclient.chat

import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper

class ChatMiddleware(
    private val apiHelper: ApiHelper
) : Middleware<ChatState> {
    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: ChatState,
        newState: ChatState
    ) {
        when (action) {
            else -> {
            }
        }
    }
}
