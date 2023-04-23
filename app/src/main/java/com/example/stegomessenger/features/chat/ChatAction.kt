package com.example.stegomessenger.features.chat

import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.data.chat.Chat
import com.example.stegomessenger.features.chat.items.ImageMessageCell

sealed class ChatAction : Action {
    data class Init(val chat: Chat) : ChatAction()
    object ClickSendImage : ChatAction()
    object ClickSendText : ChatAction()
    data class ClickImage(val cell: ImageMessageCell) : ChatAction()
}
