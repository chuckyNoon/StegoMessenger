package com.example.stegomessenger.search

import com.example.stegomessenger.arch.adapter.DelegateDiffable
import com.example.stegomessenger.arch.redux.util.Event
import com.example.stegomessenger.search.model.MatchingUser

data class SearchState(
    val typedText: String?,
    val matchingUsers: List<MatchingUser>,
    val viewState: SearchViewState,
    val backEvent: Event<Unit>?
) {
    companion object {
        val INITIAL = SearchState(
            typedText = null,
            matchingUsers = emptyList(),
            viewState = SearchViewState.INITIAL,
            backEvent = null
        )
    }
}

data class SearchViewState(
    val searchText: String?,
    val cells: List<DelegateDiffable<*>>
) {
    companion object {
        val INITIAL = SearchViewState(
            searchText = null,
            cells = emptyList()
        )
    }
}
