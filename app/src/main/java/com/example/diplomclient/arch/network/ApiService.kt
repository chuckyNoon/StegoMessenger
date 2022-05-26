package com.example.diplomclient.arch.network

import com.example.diplomclient.arch.network.model.*
import okhttp3.MultipartBody
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

    @Multipart
    @POST("sendimage")
    suspend fun sendImage(
        @Part file: MultipartBody.Part,
        @Part receiverId: MultipartBody.Part
    )

    @POST("search")
    @FormUrlEncoded
    suspend fun search(@Field("text") text: String): SearchResponse
}
