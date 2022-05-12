package com.example.diplomclient.arch.network

class ApiHelper(private val apiService: ApiService) {

    suspend fun getChats() =
        apiService.getChats()

    suspend fun doLogin(login: String, password: String) =
        apiService.doLogin(login, password)

    suspend fun doRegister(login: String, password: String) =
        apiService.doRegister(login, password)

    suspend fun startChat(id: String) =
        apiService.startChat(id)

    suspend fun search(text: String) =
        apiService.search(text)
}
