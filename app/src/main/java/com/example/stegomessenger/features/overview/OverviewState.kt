package com.example.stegomessenger.features.overview

import com.example.stegomessenger.arch.adapter.DelegateDiffable
import com.example.stegomessenger.data.chat.Chat

data class OverviewState(
    val chats: List<Chat>,
    val isLoading: Boolean,
    val viewState: OverviewViewState,
) {
    companion object {
        val EMPTY =
            OverviewState(
                chats = emptyList(),
                isLoading = false,
                viewState = OverviewViewState.EMPTY
            )
    }
}

data class OverviewViewState(
    val isLoading: Boolean,
    val cells: List<DelegateDiffable<*>>,
) {
    companion object {
        val EMPTY = OverviewViewState(cells = emptyList(), isLoading = true)
    }
}
