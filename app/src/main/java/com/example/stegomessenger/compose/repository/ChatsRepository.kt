package com.example.stegomessenger.compose.repository

import com.example.stegomessenger.overview.model.Chat

interface ChatsRepository {

    suspend fun fetchChats(): List<Chat>?

    suspend fun addChat(receiverId: String, text: String)

    suspend fun findChatById(id: String): Chat?
}