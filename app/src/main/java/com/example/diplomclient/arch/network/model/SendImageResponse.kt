package com.example.diplomclient.arch.network.model

import com.google.gson.annotations.SerializedName

data class SendImageResponse(
    @SerializedName("content")
    val base64Str: String,
    @SerializedName("size")
    val size: Int
)
