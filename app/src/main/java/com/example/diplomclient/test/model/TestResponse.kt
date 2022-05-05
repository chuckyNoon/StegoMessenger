package com.example.diplomclient.test.model

import com.google.gson.annotations.SerializedName

data class TestResponse(
    @SerializedName("value")
    val value: String
)
