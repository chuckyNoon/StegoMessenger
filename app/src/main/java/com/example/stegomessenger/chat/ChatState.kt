package com.example.stegomessenger.chat

import com.example.stegomessenger.arch.adapter.DelegateDiffable
import com.example.stegomessenger.arch.redux.util.Event
import com.example.stegomessenger.overview.model.Chat

data class ChatState(
    val chat: Chat?,
    val viewState: ChatViewState,
    val completeEvent: Event<Unit>?
) {
    companion object {
        val INITIAL = ChatState(
            chat = null,
            viewState = ChatViewState.INITIAL,
            completeEvent = null
        )
    }
}

data class ChatViewState(
    val chatName: String?,
    val cells: List<DelegateDiffable<*>>
) {
    companion object {
        val INITIAL = ChatViewState(chatName = null, cells = emptyList())
    }
}
