package com.example.features.search

import com.example.data.matching_user.MatchingUser

data class SearchState(
    val typedText: String?,
    val matchingUsers: List<MatchingUser>,
    val viewState: SearchViewState,
) {
    companion object {
        val INITIAL = SearchState(
            typedText = null,
            matchingUsers = emptyList(),
            viewState = SearchViewState.INITIAL,
        )
    }
}

data class SearchViewState(
    val searchText: String?,
    val cells: List<*>
) {
    companion object {
        val INITIAL = SearchViewState(
            searchText = null,
            cells = emptyList<Any>()
        )
    }
}
