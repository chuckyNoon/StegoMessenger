package com.example.stegomessenger.compose.feature.search

import com.example.stegomessenger.search.item.SearchUserCell
import com.example.stegomessenger.search.model.MatchingUser

sealed class SearchIntent {
    data class TextTyped(val text: String) : SearchIntent()
    data class ClickStartChat(val cell: SearchUserCell) : SearchIntent()
    object Back : SearchIntent()
}