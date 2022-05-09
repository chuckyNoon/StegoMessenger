package com.example.diplomclient.chat

import com.aita.adapter.composable.DelegateDiffable
import com.example.diplomclient.overview.model.Chat

data class ChatState(
    val chat: Chat?,
    val typedText: String?,
    val viewState: ChatViewState
) {
    companion object {
        val EMPTY = ChatState(
            chat = null,
            typedText = null,
            viewState = ChatViewState.EMPTY
        )
    }
}

data class ChatViewState(
    val chatName: String?,
    val typedText: String?,
    val cells: List<DelegateDiffable<*>>
) {
    companion object {
        val EMPTY =
            ChatViewState(
                chatName = null,
                typedText = null,
                cells = emptyList()
            )
    }
}
