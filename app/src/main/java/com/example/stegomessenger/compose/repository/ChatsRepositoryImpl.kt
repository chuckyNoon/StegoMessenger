package com.example.stegomessenger.compose.repository

import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.overview.model.Chat

class ChatsRepositoryImpl(
    val apiService: ApiService
) : ChatsRepository {

    override suspend fun fetchChats(): List<Chat>? {
        return try {
            apiService.getChats(true).chats
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    override suspend fun addChat(
        receiverId: String,
        text: String
    ) {
        apiService.sendText(receiverId, text)
    }

    override suspend fun findChatById(id: String): Chat? {
        return fetchChats()?.firstOrNull { it.id == id }
    }
}