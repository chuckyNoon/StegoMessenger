package com.example.diplomclient.arch.network

import com.example.diplomclient.arch.network.model.DataResponse
import com.example.diplomclient.arch.network.model.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("text")
    suspend fun getData(): DataResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("login") login: String,
        @Field("password") password: String
    ): TokenResponse

    @POST("register")
    @FormUrlEncoded
    suspend fun doRegister(
        @Field("login") login: String,
        @Field("password") password: String
    ): TokenResponse
}
