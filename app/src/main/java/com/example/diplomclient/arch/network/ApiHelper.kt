package com.example.diplomclient.arch.network

import com.example.diplomclient.arch.network.model.SendImageResponse

class ApiHelper(private val apiService: ApiService) {

    suspend fun getChats(isForced: Boolean) =
        apiService.getChats(isForced)

    suspend fun doLogin(id: String, password: String) =
        apiService.doLogin(id, password)

    suspend fun doRegister(
        id: String,
        password: String,
        name: String
    ) =
        apiService.doRegister(
            id = id,
            password = password,
            name = name
        )

    suspend fun sendText(receiverId: String, text: String) =
        apiService.sendText(
            receiverId = receiverId,
            text = text
        )

    suspend fun sendImage(
        receiverId: String,
        imageStr: String
    ): SendImageResponse =
        apiService.sendImage(
            receiverId = receiverId,
            imageStr = imageStr
        )

    suspend fun search(text: String) =
        apiService.search(text)
}
