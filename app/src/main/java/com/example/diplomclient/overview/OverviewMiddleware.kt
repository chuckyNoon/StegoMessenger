package com.example.diplomclient.overview

import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.chat.ChatAction
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.main.navigation.CoreNavAction
import com.example.diplomclient.overview.model.Chat
import com.example.diplomclient.overview.model.Message

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
            is OverviewAction.LoadChats -> loadChatsTest(dispatchable) // loadChats()
            is OverviewAction.ClickChat -> handleClickChat(newState, action, dispatchable)
        }
    }

    private fun loadChats(dispatchable: Dispatchable) {
        dispatchable.dispatch(OverviewAction.ChatsLoadingStarted)
        launchBackgroundWork {
            safeApiCall(
                apiCall = { apiHelper.getChats() },
                onSuccess = {
                    dispatchable.dispatch(OverviewAction.ChatsLoadingSuccess(it.chats))
                },
                onError = {
                    dispatchable.dispatch(CoreNavAction.ShowError(it.message ?: "f2"))
                    dispatchable.dispatch(OverviewAction.ChatsLoadingFail)
                }
            )
        }
    }

    private fun loadChatsTest(dispatchable: Dispatchable) {
        val testMessages = listOf(
            Message(
                text = "123123",
                createdAtUtcSeconds = 1231230,
                isMine = true
            ),
            Message(
                text = "12",
                createdAtUtcSeconds = 123123,
                isMine = true
            ),
            Message(
                text = "124",
                createdAtUtcSeconds = 1231230,
                isMine = true
            )
        )
        val chats = listOf(
            Chat(
                id = "1",
                name = "first chat",
                messages = testMessages
            ),
            Chat(
                id = "2",
                name = "first chat3",
                messages = testMessages
            ),
            Chat(
                id = "3",
                name = "first cha323t",
                messages = testMessages
            ),
            Chat(
                id = "4",
                name = "fi324rst chat",
                messages = testMessages
            )
        )
        dispatchable.dispatch(OverviewAction.ChatsLoadingSuccess(chats))
    }

    private fun handleClickChat(
        newState: OverviewState,
        action: OverviewAction.ClickChat,
        dispatchable: Dispatchable
    ) {
        val clickedCell = action.cell
        val clickedChat = newState.chats.first { it.id == clickedCell.id }

        dispatchable.dispatch(CoreNavAction.ShowChat)
        dispatchable.dispatch(ChatAction.Init(clickedChat))
    }
}
