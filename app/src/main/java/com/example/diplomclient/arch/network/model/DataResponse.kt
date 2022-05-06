package com.example.diplomclient.arch.network.model

import com.google.gson.annotations.SerializedName

data class DataResponse(
    @SerializedName("value")
    val value: String
)
