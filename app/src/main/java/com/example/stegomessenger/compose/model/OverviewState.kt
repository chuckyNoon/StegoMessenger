package com.example.stegomessenger.compose.model

import com.example.stegomessenger.arch.adapter.DelegateDiffable
import com.example.stegomessenger.overview.model.Chat

data class OverviewState(
    val chats: List<Chat>,
    val isLoading: Boolean,
    val viewState: OverviewViewState? = null,
) {
    companion object {
        val INITIAL =
            OverviewState(
                chats = emptyList(),
                isLoading = false,
                viewState = OverviewViewState.INITIAL
            )
    }
}

data class OverviewViewState(
    val isLoading: Boolean,
    val cells: List<DelegateDiffable<*>>,
) {
    companion object {
        val INITIAL = OverviewViewState(cells = emptyList(), isLoading = true)
    }
}
