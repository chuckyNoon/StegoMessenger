package com.example.stegomessenger.chat

import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.chat.items.ImageMessageCell
import com.example.stegomessenger.data.chat.Chat

sealed class ChatAction : Action {
    data class Init(val chat: Chat) : ChatAction()
    object ClickSendImage : ChatAction()
    object ClickSendText : ChatAction()
    data class ClickImage(val cell: ImageMessageCell): ChatAction()
}
