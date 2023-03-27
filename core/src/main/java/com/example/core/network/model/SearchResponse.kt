package com.example.core.network.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("users")
    val matchingUsers: List<MatchingUserDTO>
)

data class MatchingUserDTO(
    val id: String,
    val name: String
)
