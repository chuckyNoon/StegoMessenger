package com.example.stegomessenger.search

import com.example.stegomessenger.arch.adapter.DelegateDiffable
import com.example.stegomessenger.arch.redux.util.Event
import com.example.stegomessenger.data.search.SearchResult

data class SearchState(
    val typedText: String?,
    val searchResults: List<SearchResult>,
    val viewState: SearchViewState,
    val backEvent: Event<Unit>?
) {
    companion object {
        val EMPTY = SearchState(
            typedText = null,
            searchResults = emptyList(),
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
