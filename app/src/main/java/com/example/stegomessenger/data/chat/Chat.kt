package com.example.stegomessenger.data.chat

import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("messages")
    val messages: List<Message>
)
