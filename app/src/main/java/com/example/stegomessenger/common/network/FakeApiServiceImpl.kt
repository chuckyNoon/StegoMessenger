package com.example.stegomessenger.common.network

import com.example.stegomessenger.common.network.model.ChatsResponse
import com.example.stegomessenger.common.network.model.SearchResponse
import com.example.stegomessenger.common.network.model.TokenResponse
import com.example.stegomessenger.data.chat.Chat
import com.example.stegomessenger.data.chat.Message
import com.example.stegomessenger.data.search.SearchResult
import kotlinx.coroutines.delay
import okhttp3.MultipartBody

class FakeApiServiceImpl : ApiService {
    private val messages = listOf(
        Message(
            text = "Text1",
            createdAtUtcSeconds = 0,
            isMine = false,
            imageUrl = ""
        ),
        Message(
            text = "",
            createdAtUtcSeconds = 0,
            isMine = false,
            imageUrl = "https://picsum.photos/200"
        ),

        )

    private var chats = (0..10).map {
        Chat(id = it.toString(), name = "User ${it}", messages = messages)
    }.toMutableList()



    private val delayMillis = 1000L

    override suspend fun getChats(isForced: Boolean): ChatsResponse {
        delay(delayMillis)
        return ChatsResponse(chats)
    }

    override suspend fun logIn(id: String, password: String): TokenResponse {
        delay(delayMillis)
        return TokenResponse("token")
    }

    override suspend fun register(id: String, password: String, name: String): TokenResponse {
        delay(delayMillis)
        return TokenResponse("token")
    }

    override suspend fun sendText(receiverId: String, text: String) {
        delay(delayMillis)
        val newMessage = Message(
            text = "text",
            createdAtUtcSeconds = System.currentTimeMillis() * 1000,
            isMine = true,
            imageUrl = ""
        )
        val chat = chats.first { it.id == receiverId }
        val newChat = chat.copy(messages = chat.messages + newMessage)
        chats[chats.indexOf(chat)] = newChat
    }

    override suspend fun sendImage(file: MultipartBody.Part, receiverId: MultipartBody.Part) {

    }

    override suspend fun search(text: String): SearchResponse {
        return SearchResponse(
            searchResults = listOf(
                SearchResult(
                    id = "{$text}1",
                    name = "User1"
                ),
                SearchResult(
                    id = "{$text}2",
                    name = "User2"
                ),
                SearchResult(
                    id = "{$text}3",
                    name = "User3"
                )
            )
        )
    }
}