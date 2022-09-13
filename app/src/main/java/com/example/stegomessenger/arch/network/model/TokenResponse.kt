package com.example.stegomessenger.arch.network.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val value: String
)
