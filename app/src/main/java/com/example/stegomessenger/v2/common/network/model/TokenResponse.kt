package com.example.stegomessenger.v2.common.network.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val value: String
)
