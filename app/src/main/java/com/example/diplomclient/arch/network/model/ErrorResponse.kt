package com.example.diplomclient.arch.network.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    val code: Int? = null,
    val message: String? = null
)

data class ErrorMessage(
    @SerializedName("text")
    val message: String?
)
