package com.example.stegomessenger.common.network

import com.example.stegomessenger.common.network.model.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @POST("chats")
    @FormUrlEncoded
    suspend fun getChats(@Field("forced") isForced: Boolean): ChatsResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun logIn(
        @Field("id") id: String,
        @Field("password") password: String
    ): TokenResponse

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("id") id: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): TokenResponse

    @POST("sendText")
    @FormUrlEncoded
    suspend fun sendText(
        @Field("receiverId") receiverId: String,
        @Field("text") text: String
    )

    @Multipart
    @POST("sendImage")
    suspend fun sendImage(
        @Part file: MultipartBody.Part,
        @Part receiverId: MultipartBody.Part
    )

    @POST("search")
    @FormUrlEncoded
    suspend fun search(@Field("text") query: String): SearchResponse
}
