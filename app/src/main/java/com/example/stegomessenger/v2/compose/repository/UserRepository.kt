package com.example.stegomessenger.v2.compose.repository

import com.example.stegomessenger.v2.common.network.ApiService


class UserRepository(
    private val apiService: ApiService
) {
    fun getCurrentUser(){

    }

    suspend fun addUser(login: String, password: String, name: String){
        apiService.doRegister(
            id = login,
            password = password,
            name = name
        ).value
    }
}