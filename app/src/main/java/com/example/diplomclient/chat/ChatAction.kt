package com.example.diplomclient.chat

import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.overview.model.Chat

sealed class ChatAction : Action {
    data class Init(val chat: Chat) : ChatAction()
    data class TextTyped(val text: String) : ChatAction()
    object ClickSend : ChatAction()
    object CompleteSending : ChatAction()
}
