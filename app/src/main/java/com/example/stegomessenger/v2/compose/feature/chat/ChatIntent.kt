package com.example.stegomessenger.v2.compose.feature.chat

import com.example.stegomessenger.v2.compose.model.ImageMessageCell


sealed class ChatIntent {
    object ClickSendImage : ChatIntent()
    object ClickSendText : ChatIntent()
    data class ClickImage(val cell: ImageMessageCell): ChatIntent()
}