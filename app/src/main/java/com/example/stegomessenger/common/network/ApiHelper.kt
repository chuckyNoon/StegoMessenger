package com.example.stegomessenger.common.network

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ApiHelper(private val apiService: ApiService) {

    suspend fun getChats(isForced: Boolean) =
        apiService.getChats(isForced)

    suspend fun doLogin(id: String, password: String) =
        apiService.doLogin(id, password)

    suspend fun doRegister(
        id: String,
        password: String,
        name: String
    ) =
        apiService.doRegister(
            id = id,
            password = password,
            name = name
        )

    suspend fun sendText(receiverId: String, text: String) =
        apiService.sendText(
            receiverId = receiverId,
            text = text
        )

    suspend fun sendImage(
        receiverId: String,
        imageFile: File
    ) =
        apiService.sendImage(
            file = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                RequestBody.create(MediaType.parse("image/*"), imageFile)
            ),
            receiverId = MultipartBody.Part.createFormData("receiverId", receiverId)
        )

    suspend fun search(text: String) =
        apiService.search(text)
}
