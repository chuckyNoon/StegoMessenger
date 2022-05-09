package com.example.diplomclient.overview

import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.overview.model.Chat
import com.example.diplomclient.overview.model.ChatCell

sealed class OverviewAction : Action {
    object LoadChats : OverviewAction()
    object ChatsLoadingStarted : OverviewAction()
    data class ChatsLoadingSuccess(val chats: List<Chat>) : OverviewAction()
    object ChatsLoadingFail : OverviewAction()
    object ClickPlus : OverviewAction()
    data class ClickChat(val cell: ChatCell) : OverviewAction()
}
