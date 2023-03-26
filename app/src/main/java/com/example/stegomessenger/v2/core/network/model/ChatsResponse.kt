package com.example.stegomessenger.v2.core.network.model

import com.example.stegomessenger.v2.data.chat.Chat
import com.google.gson.annotations.SerializedName

class ChatsResponse(
    @SerializedName("chats")
    val chats: List<Chat>
)