package com.example.diplomclient.arch.network

import com.example.diplomclient.arch.network.model.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("chats")
    suspend fun getChats(): ChatsResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("login") login: String,
        @Field("password") password: String
    ): TokenResponse

    @POST("registration")
    @FormUrlEncoded
    suspend fun doRegister(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("id") id: String,
        @Field("name") name: String
    ): TokenResponse

    @POST("send")
    @FormUrlEncoded
    suspend fun sendText(
        @Field("receiverId") receiverId: String,
        @Field("text") text: String
    )

    @POST("search")
    @FormUrlEncoded
    suspend fun search(@Field("text") text: String): SearchResponse
}
