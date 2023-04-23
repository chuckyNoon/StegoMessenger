package com.example.stegomessenger.features.search

import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.features.search.item.SearchUserCell
import com.example.stegomessenger.data.search.SearchResult

sealed class SearchAction : Action {
    data class TextTyped(val text: String) : SearchAction()
    data class ClickStartChat(val cell: SearchUserCell) : SearchAction()
    data class UsersLoaded(val searchResults: List<SearchResult>) : SearchAction()
    object Back : SearchAction()
}
