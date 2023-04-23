package com.example.stegomessenger.common.network.model

import com.example.stegomessenger.data.search.SearchResult
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("users")
    val searchResults: List<SearchResult>
)
