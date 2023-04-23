package com.example.stegomessenger.features.overview

import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.features.chat.ChatAction
import com.example.stegomessenger.app.core.CoreAction
import javax.inject.Inject

class OverviewMiddleware @Inject constructor() : Middleware<OverviewState>() {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: OverviewState,
        newState: OverviewState
    ) {
        when (action) {
            is OverviewAction.ClickChat -> handleClickChat(newState, action, dispatchable)
        }
    }

    private fun handleClickChat(
        newState: OverviewState,
        action: OverviewAction.ClickChat,
        dispatchable: Dispatchable
    ) {
        val clickedCell = action.cell
        val clickedChat = newState.chats.first { it.id == clickedCell.id }

        dispatchable.dispatch(CoreAction.ShowChat)
        dispatchable.dispatch(ChatAction.Init(clickedChat))
    }
}
