package com.example.features.chat

import com.example.core.design.items.image_message.ImageMessageCell


sealed class ChatIntent {
    object ClickSendImage : ChatIntent()
    object ClickSendText : ChatIntent()
    data class ClickImage(val cell: ImageMessageCell): ChatIntent()
}