package com.example.diplomclient.arch.network

class ApiHelper(private val apiService: ApiService) {

    suspend fun getResponse() = apiService.getData()

    suspend fun getToken(name: String, password: String) =
        apiService.getToken(name, password)
}
