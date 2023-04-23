package com.example.stegomessenger.data.chat

import com.example.stegomessenger.common.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getChats(isForced: Boolean) = withContext(Dispatchers.IO) {
        apiService.getChats(isForced)
    }

    suspend fun startNewChat(userId: String) = withContext(Dispatchers.IO){
        apiService.sendText(receiverId = userId, text = "")
    }
}