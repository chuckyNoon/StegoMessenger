package com.example.diplomclient.chat

import com.example.diplomclient.arch.adapter.composable.DelegateDiffable
import com.example.diplomclient.arch.redux.util.Event
import com.example.diplomclient.overview.model.Chat

data class ChatState(
    val chat: Chat?,
    val viewState: ChatViewState,
    val completeEvent: Event<Unit>?
) {
    companion object {
        val EMPTY = ChatState(
            chat = null,
            viewState = ChatViewState.EMPTY,
            completeEvent = null
        )
    }
}

data class ChatViewState(
    val chatName: String?,
    val cells: List<DelegateDiffable<*>>
) {
    companion object {
        val EMPTY =
            ChatViewState(
                chatName = null,
                cells = emptyList()
            )
    }
}
