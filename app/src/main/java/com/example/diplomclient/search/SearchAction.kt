package com.example.diplomclient.search

import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.search.item.SearchUserCell
import com.example.diplomclient.search.model.MatchingUser

sealed class SearchAction : Action {
    data class TextTyped(val text: String) : SearchAction()
    data class ClickStartChat(val cell: SearchUserCell) : SearchAction()
    data class UsersLoaded(val matchingUsers: List<MatchingUser>) : SearchAction()
    object Back : SearchAction()
    object Complete : SearchAction()
}
