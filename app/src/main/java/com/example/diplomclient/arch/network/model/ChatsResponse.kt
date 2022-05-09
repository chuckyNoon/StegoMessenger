package com.example.diplomclient.arch.network.model

import com.example.diplomclient.overview.model.Chat
import com.google.gson.annotations.SerializedName

class ChatsResponse(
    @SerializedName("chats")
    val chats: List<Chat>
)