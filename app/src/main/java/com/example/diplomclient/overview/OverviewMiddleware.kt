package com.example.diplomclient.overview

import com.example.diplomclient.arch.redux.dispatcher.Dispatchable
import com.example.diplomclient.arch.redux.store.Middleware
import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.chat.ChatAction
import com.example.diplomclient.main.navigation.CoreAction

class OverviewMiddleware(
    private val apiHelper: ApiHelper
) : Middleware<OverviewState> {

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
