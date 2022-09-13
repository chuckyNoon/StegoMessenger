package com.example.stegomessenger.content_dialog

import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action

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
