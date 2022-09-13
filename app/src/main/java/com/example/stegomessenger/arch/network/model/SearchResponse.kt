package com.example.stegomessenger.arch.network.model

import com.example.stegomessenger.search.model.MatchingUser
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("users")
    val matchingUsers: List<MatchingUser>
)
