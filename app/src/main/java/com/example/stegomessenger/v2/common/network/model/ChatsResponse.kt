package com.example.stegomessenger.v2.common.network.model

import com.example.stegomessenger.v2.common.model.Chat
import com.google.gson.annotations.SerializedName

class ChatsResponse(
    @SerializedName("chats")
    val chats: List<Chat>
)