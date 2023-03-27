package com.example.core.network.model

import com.google.gson.annotations.SerializedName

class ChatsResponse(
    @SerializedName("chats")
    val chats: List<ChatDTO>
)

data class ChatDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("messages")
    val messages: List<MessageDTO>
)

data class MessageDTO(
    @SerializedName("text")
    val text: String,
    @SerializedName("date")
    val createdAtUtcSeconds: Long,
    @SerializedName("mine")
    val isMine: Boolean,
    @SerializedName("image")
    val imageUrl: String
)