package com.example.diplomclient.arch.network

import com.example.diplomclient.arch.network.model.*
import retrofit2.http.*

interface ApiService {

    @POST("chats")
    @FormUrlEncoded
    suspend fun getChats(@Field("forced") isForced: Boolean): ChatsResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("id") id: String,
        @Field("password") password: String
    ): TokenResponse

    @POST("registration")
    @FormUrlEncoded
    suspend fun doRegister(
        @Field("id") id: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): TokenResponse

    @POST("send")
    @FormUrlEncoded
    suspend fun sendText(
        @Field("receiverId") receiverId: String,
        @Field("text") text: String
    )

    @POST("image")
    @FormUrlEncoded
    suspend fun sendImage(
        @Field("receiverId") receiverId: String,
        @Field("image") imageStr: String
    ): SendImageResponse

    @POST("search")
    @FormUrlEncoded
    suspend fun search(@Field("text") text: String): SearchResponse
}
