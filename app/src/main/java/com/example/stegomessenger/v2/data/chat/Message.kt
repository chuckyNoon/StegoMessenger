package com.example.stegomessenger.v2.data.chat

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("text")
    val text: String,
    @SerializedName("date")
    val createdAtUtcSeconds: Long,
    @SerializedName("mine")
    val isMine: Boolean,
    @SerializedName("image")
    val imageUrl: String
)
