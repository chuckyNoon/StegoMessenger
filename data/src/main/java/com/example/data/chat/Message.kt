package com.example.data.chat

import com.example.core.network.model.MessageDTO

data class Message(
    val text: String,
    val createdAtUtcSeconds: Long,
    val isMine: Boolean,
    val imageUrl: String
){
    companion object{
        fun fromDTO(dto: MessageDTO): Message =
            Message(
                text = dto.text,
                createdAtUtcSeconds = dto.createdAtUtcSeconds,
                isMine = dto.isMine,
                imageUrl = dto.imageUrl
            )
    }
}
