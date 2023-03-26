package com.example.stegomessenger.v2.compose.feature.search

import com.example.stegomessenger.v2.compose.model.SearchUserCell

sealed class SearchIntent {
    data class TextTyped(val text: String) : SearchIntent()
    data class ClickStartChat(val cell: SearchUserCell) : SearchIntent()
    object Back : SearchIntent()
}