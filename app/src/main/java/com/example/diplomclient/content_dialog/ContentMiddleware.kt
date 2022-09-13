package com.example.diplomclient.content_dialog

import com.example.diplomclient.arch.redux.dispatcher.Dispatchable
import com.example.diplomclient.arch.redux.store.Middleware
import com.example.diplomclient.arch.redux.Action

class ContentMiddleware : Middleware<ContentState> {
    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: ContentState,
        newState: ContentState
    ) {
        // TODO: implement
    }
}
