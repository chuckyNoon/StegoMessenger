package com.example.data.chat

import com.example.core.network.model.ChatDTO

data class Chat(
    val id: String,
    val name: String,
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
