package com.example.diplomclient.arch.network.model

import com.example.diplomclient.search.model.MatchingUser
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("users")
    val matchingUsers: List<MatchingUser>
)
