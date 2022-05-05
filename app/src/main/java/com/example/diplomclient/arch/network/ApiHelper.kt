package com.example.diplomclient.arch.network

import com.example.diplomclient.test.network.ApiService

class ApiHelper(private val apiService: ApiService) {

    suspend fun getResponse() = apiService.getResponse()
}
