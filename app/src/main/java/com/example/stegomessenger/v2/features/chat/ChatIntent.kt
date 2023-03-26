package com.example.stegomessenger.v2.features.chat

import com.example.stegomessenger.v2.common.model.ImageMessageCell


sealed class ChatIntent {
    object ClickSendImage : ChatIntent()
    object ClickSendText : ChatIntent()
    data class ClickImage(val cell: ImageMessageCell): ChatIntent()
}