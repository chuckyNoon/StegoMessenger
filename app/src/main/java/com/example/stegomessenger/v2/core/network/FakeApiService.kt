package com.example.stegomessenger.v2.core.network

import com.example.stegomessenger.v2.core.network.model.ChatsResponse
import com.example.stegomessenger.v2.core.network.model.SearchResponse
import com.example.stegomessenger.v2.core.network.model.TokenResponse
import com.example.stegomessenger.v2.data.chat.Chat
import com.example.stegomessenger.v2.data.matching_user.MatchingUser
import com.example.stegomessenger.v2.data.chat.Message
import kotlinx.coroutines.delay
import okhttp3.MultipartBody

class FakeApiService : ApiService {

    private val messages = listOf(
        Message(
            text = "First message",
            createdAtUtcSeconds = 1,
            isMine = true,
            imageUrl = ""
        ),
        Message(
            text = "First1 message",
            createdAtUtcSeconds = 1,
            isMine = false,
            imageUrl = ""
        ),
        Message(
            text = "First2 message",
            createdAtUtcSeconds = 1,
            isMine = false,
            imageUrl = ""
        ),
        Message(
            text = "First3 message",
            createdAtUtcSeconds = 1,
            isMine = true,
            imageUrl = ""
        )
    )

    override suspend fun getChats(isForced: Boolean): ChatsResponse =
        ChatsResponse(
            chats = (0..30).map {
                Chat(id = it.toString(), name = "Chat ${it}", messages = messages)
            }
        )

    override suspend fun doLogin(id: String, password: String): TokenResponse =
        imitateHeavyWork {
            TokenResponse(value = TEST_TOKEN)
        }

    override suspend fun doRegister(id: String, password: String, name: String): TokenResponse =
        imitateHeavyWork {
            TokenResponse(value = TEST_TOKEN)
        }

    override suspend fun sendText(receiverId: String, text: String) = imitateHeavyWork {}

    override suspend fun sendImage(file: MultipartBody.Part, receiverId: MultipartBody.Part) =
        imitateHeavyWork {}

    override suspend fun search(text: String): SearchResponse =
        SearchResponse(
            matchingUsers = listOf(
                MatchingUser(
                    id = "1",
                    name = "${text} 1"
                ),
                MatchingUser(
                    id = "2",
                    name = "${text} 2"
                ),
                MatchingUser(
                    id = "3",
                    name = "${text} 3"
                )
            )
        )

    private suspend fun <T> imitateHeavyWork(
        postBack: () -> T
    ): T {
        delay(DELAY_MILLIS)
        return postBack()
    }

    companion object {
        private val DELAY_MILLIS = 1000L
        val TEST_TOKEN = "test_token"
    }
}