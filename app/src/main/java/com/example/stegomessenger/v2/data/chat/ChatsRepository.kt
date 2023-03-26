package com.example.stegomessenger.v2.data.chat


interface ChatsRepository {

    suspend fun fetchChats(): List<Chat>?

    suspend fun addChat(receiverId: String, text: String)

    suspend fun findChatById(id: String): Chat?
}