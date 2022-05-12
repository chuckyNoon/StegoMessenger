package com.example.diplomclient.main.navigation

import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.overview.model.Chat

sealed class CoreAction : Action {
    object ShowOverviewFragment : CoreAction()
    object ShowLogin : CoreAction()
    object ShowRegistration : CoreAction()
    object ShowChat : CoreAction()
    object ShowSearch : CoreAction()
    data class ShowError(val text: String) : CoreAction()
    object ReloadChats : CoreAction()
    data class ChatsReloaded(val chats: List<Chat>) : CoreAction()
}