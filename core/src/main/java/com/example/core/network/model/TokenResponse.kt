package com.example.core.network.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val value: String
)
