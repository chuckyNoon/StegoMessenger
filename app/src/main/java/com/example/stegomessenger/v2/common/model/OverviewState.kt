package com.example.stegomessenger.v2.common.model

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
    val cells: List<*>,
) {
    companion object {
        val INITIAL = OverviewViewState(cells = emptyList<Any>(), isLoading = true)
    }
}
