package com.example.diplomclient.test.network

import com.example.diplomclient.test.model.TestResponse
import retrofit2.http.GET

interface ApiService {

    @GET("text")
    suspend fun getResponse(): TestResponse
}
