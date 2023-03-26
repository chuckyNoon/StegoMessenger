package com.example.stegomessenger.v2.data.chat

import com.example.stegomessenger.v2.common.model.Chat


interface ChatsRepository {

    suspend fun fetchChats(): List<Chat>?

    suspend fun addChat(receiverId: String, text: String)

    suspend fun findChatById(id: String): Chat?
}