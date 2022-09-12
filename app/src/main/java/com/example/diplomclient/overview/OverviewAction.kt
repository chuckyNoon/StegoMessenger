package com.example.diplomclient.overview

import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.overview.model.Chat
import com.example.diplomclient.overview.model.items.ChatCell

sealed class OverviewAction : Action {
    object ChatsLoadingStarted : OverviewAction()
    data class ChatsLoadingSuccess(val chats: List<Chat>) : OverviewAction()
    object ChatsLoadingFail : OverviewAction()
    data class ClickChat(val cell: ChatCell) : OverviewAction()
}
