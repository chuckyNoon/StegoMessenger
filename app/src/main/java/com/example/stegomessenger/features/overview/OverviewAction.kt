package com.example.stegomessenger.features.overview

import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.features.overview.items.ChatCell

sealed class OverviewAction : Action {
    object ChatsLoadingStarted : OverviewAction()
    object ChatsLoadingFail : OverviewAction()
    data class ClickChat(val cell: ChatCell) : OverviewAction()
}
