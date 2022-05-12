package com.example.diplomclient.arch.network.model

import com.example.diplomclient.overview.model.Chat
import com.google.gson.annotations.SerializedName

data class AddChatResponse(
    @SerializedName("chat")
    val chat: Chat
)
