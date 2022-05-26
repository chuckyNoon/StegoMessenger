package com.example.diplomclient.chat

import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.overview.model.Chat

sealed class ChatAction : Action {
    data class Init(val chat: Chat) : ChatAction()
    object ClickSendImage : ChatAction()
    object ClickImage : ChatAction()
    object ClickSendText : ChatAction()
    object CompleteSending : ChatAction()
}
