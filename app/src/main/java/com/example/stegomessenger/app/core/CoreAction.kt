package com.example.stegomessenger.app.core

import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.data.chat.Chat

sealed class CoreAction : Action {
    object ShowOverviewFragment : CoreAction()
    object ShowLogin : CoreAction()
    object ShowRegistration : CoreAction()
    object ShowChat : CoreAction()
    object ShowSearch : CoreAction()
    object ShowResult : CoreAction()
    object ShowStegoDialog : CoreAction()
    object ShowContentDialog : CoreAction()
    data class ShowToast(val text: String) : CoreAction()
    object ReloadChats : CoreAction()
    data class ChatsReloaded(val chats: List<Chat>) : CoreAction()
    object SyncWithServer : CoreAction()
}
