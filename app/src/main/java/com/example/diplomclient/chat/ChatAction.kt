package com.example.diplomclient.chat

import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.chat.items.ImageMessageCell
import com.example.diplomclient.overview.model.Chat

sealed class ChatAction : Action {
    data class Init(val chat: Chat) : ChatAction()
    object ClickSendImage : ChatAction()
    object ClickSendText : ChatAction()
    data class ClickImage(val cell: ImageMessageCell): ChatAction()
}
