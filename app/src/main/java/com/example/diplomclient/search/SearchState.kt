package com.example.diplomclient.search

import com.example.diplomclient.arch.adapter.composable.DelegateDiffable
import com.example.diplomclient.arch.redux.util.Event
import com.example.diplomclient.search.model.MatchingUser

data class SearchState(
    val typedText: String?,
    val matchingUsers: List<MatchingUser>,
    val viewState: SearchViewState,
    val backEvent: Event<Unit>?
) {
    companion object {
        val EMPTY = SearchState(
            typedText = null,
            matchingUsers = emptyList(),
            viewState = SearchViewState.EMPTY,
            backEvent = null
        )
    }
}

data class SearchViewState(
    val searchText: String?,
    val cells: List<DelegateDiffable<*>>
) {
    companion object {
        val EMPTY = SearchViewState(
            searchText = null,
            cells = emptyList()
        )
    }
}
