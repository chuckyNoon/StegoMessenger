package com.example.stegomessenger.data.chat

import com.example.stegomessenger.common.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

class ChatsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ChatsRepository {
    override suspend fun getChats(isForced: Boolean): List<Chat> =
        withContext(Dispatchers.IO) {
            apiService.getChats(isForced).chats
        }

    override suspend fun startNewChat(userId: String) = withContext(Dispatchers.IO) {
        apiService.sendText(receiverId = userId, text = "")
    }
}