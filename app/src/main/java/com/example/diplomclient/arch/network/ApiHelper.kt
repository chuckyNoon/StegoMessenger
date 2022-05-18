package com.example.diplomclient.arch.network

import com.example.diplomclient.arch.network.model.SendImageResponse

class ApiHelper(private val apiService: ApiService) {

    suspend fun getChats() =
        apiService.getChats()

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

    suspend fun sendImage(imageStr: String): SendImageResponse =
        apiService.sendImage(
            imageStr
        )

    suspend fun search(text: String) =
        apiService.search(text)
}
