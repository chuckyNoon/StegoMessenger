package com.example.stegomessenger.v2.data.chat

import com.example.core.network.model.MessageDTO
import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("text")
    val text: String,
    @SerializedName("date")
    val createdAtUtcSeconds: Long,
    @SerializedName("mine")
    val isMine: Boolean,
    @SerializedName("image")
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
