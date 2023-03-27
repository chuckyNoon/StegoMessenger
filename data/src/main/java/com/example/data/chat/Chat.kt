package com.example.data.chat

import com.example.core.network.model.ChatDTO
import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("messages")
    val messages: List<Message>
) {
    companion object {
        fun fromDTO(dto: ChatDTO): Chat =
            Chat(
                id = dto.id,
                name = dto.name,
                messages = dto.messages.map { Message.fromDTO(it) }
            )
    }
}
