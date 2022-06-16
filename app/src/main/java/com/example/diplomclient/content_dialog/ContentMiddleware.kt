package com.example.diplomclient.content_dialog

import com.example.diplomclient.arch.flux.dispatcher.Dispatchable
import com.example.diplomclient.arch.flux.store.Middleware
import com.example.diplomclient.arch.flux.Action

class ContentMiddleware : Middleware<ContentState> {
    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: ContentState,
        newState: ContentState
    ) {
    }
}
