package com.example.stegomessenger.v2.common.model

import com.example.stegomessenger.v2.data.chat.Chat

data class ChatState(
    val chat: Chat?,
    val viewState: ChatViewState,
) {
    companion object {
        val INITIAL = ChatState(
            chat = null,
            viewState = ChatViewState.INITIAL,
        )
    }
}

data class ChatViewState(
    val chatName: String?,
    val cells: List<Any>
) {
    companion object {
        val INITIAL = ChatViewState(chatName = null, cells = emptyList<Any>())
    }
}
