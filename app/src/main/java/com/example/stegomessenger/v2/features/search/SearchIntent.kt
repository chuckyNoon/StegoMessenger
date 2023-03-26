package com.example.stegomessenger.v2.features.search

import com.example.stegomessenger.v2.common.model.SearchUserCell

sealed class SearchIntent {
    data class TextTyped(val text: String) : SearchIntent()
    data class ClickStartChat(val cell: SearchUserCell) : SearchIntent()
    object Back : SearchIntent()
}