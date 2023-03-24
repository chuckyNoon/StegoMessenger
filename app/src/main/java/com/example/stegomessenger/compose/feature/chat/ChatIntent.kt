package com.example.stegomessenger.compose.feature.chat

import com.example.stegomessenger.chat.items.ImageMessageCell

sealed class ChatIntent {
    object ClickSendImage : ChatIntent()
    object ClickSendText : ChatIntent()
    data class ClickImage(val cell: ImageMessageCell): ChatIntent()
}