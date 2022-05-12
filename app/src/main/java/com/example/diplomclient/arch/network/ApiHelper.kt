package com.example.diplomclient.arch.network

class ApiHelper(private val apiService: ApiService) {

    suspend fun getChats() =
        apiService.getChats()

    suspend fun doLogin(login: String, password: String) =
        apiService.doLogin(login, password)

    suspend fun doRegister(
        login: String,
        password: String,
        id: String,
        name: String
    ) =
        apiService.doRegister(login, password, id, name)

    suspend fun sendText(receiverId: String, text: String) =
        apiService.sendText(
            receiverId = receiverId,
            text = text
        )

    suspend fun search(text: String) =
        apiService.search(text)
}
