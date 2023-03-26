package com.example.stegomessenger.v2.core.network.model

import com.example.stegomessenger.v2.data.matching_user.MatchingUser
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("users")
    val matchingUsers: List<MatchingUser>
)
