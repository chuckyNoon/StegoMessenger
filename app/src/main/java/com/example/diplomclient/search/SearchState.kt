package com.example.diplomclient.search

import com.aita.adapter.composable.DelegateDiffable
import com.aita.arch.util.Event
import com.example.diplomclient.search.model.MatchingUser

data class SearchState(
    val typedId: String?,
    val matchingUsers: List<MatchingUser>,
    val viewState: SearchViewState,
    val backEvent: Event<Unit>?
) {
    companion object {
        val EMPTY = SearchState(
            typedId = null,
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