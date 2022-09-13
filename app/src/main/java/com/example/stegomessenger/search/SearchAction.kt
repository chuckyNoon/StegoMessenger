package com.example.stegomessenger.search

import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.search.item.SearchUserCell
import com.example.stegomessenger.search.model.MatchingUser

sealed class SearchAction : Action {
    data class TextTyped(val text: String) : SearchAction()
    data class ClickStartChat(val cell: SearchUserCell) : SearchAction()
    data class UsersLoaded(val matchingUsers: List<MatchingUser>) : SearchAction()
    object Back : SearchAction()
}
