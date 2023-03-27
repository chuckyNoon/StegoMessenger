package com.example.data.user

import com.example.core.network.ApiService


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