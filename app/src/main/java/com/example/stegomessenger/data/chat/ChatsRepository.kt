package com.example.stegomessenger.data.chat

interface ChatsRepository {
    suspend fun getChats(isForced: Boolean): List<Chat>
    suspend fun startNewChat(userId: String)
}