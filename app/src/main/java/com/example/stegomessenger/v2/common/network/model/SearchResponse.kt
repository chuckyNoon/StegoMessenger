package com.example.stegomessenger.v2.common.network.model

import com.example.stegomessenger.v2.compose.model.MatchingUser
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("users")
    val matchingUsers: List<MatchingUser>
)
