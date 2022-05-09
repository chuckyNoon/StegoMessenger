package com.example.diplomclient.arch.network

class ApiHelper(private val apiService: ApiService) {

    suspend fun getResponse() = apiService.getData()

    suspend fun doLogin(login: String, password: String) =
        apiService.doLogin(login, password)

    suspend fun doRegister(login: String, password: String) =
        apiService.doRegister(login, password)
}
