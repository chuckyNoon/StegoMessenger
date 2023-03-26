package com.example.stegomessenger.v2.compose.repository

import com.example.stegomessenger.v2.compose.model.Chat


interface ChatsRepository {

    suspend fun fetchChats(): List<Chat>?

    suspend fun addChat(receiverId: String, text: String)

    suspend fun findChatById(id: String): Chat?
}