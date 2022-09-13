package com.example.stegomessenger.common.network.model

import com.example.stegomessenger.overview.model.Chat
import com.google.gson.annotations.SerializedName

class ChatsResponse(
    @SerializedName("chats")
    val chats: List<Chat>
)