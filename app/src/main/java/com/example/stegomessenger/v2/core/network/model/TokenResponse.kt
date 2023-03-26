package com.example.stegomessenger.v2.core.network.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val value: String
)
