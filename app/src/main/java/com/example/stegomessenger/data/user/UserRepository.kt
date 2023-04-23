package com.example.stegomessenger.data.user

interface UserRepository{
    suspend fun logIn(login: String, password: String)
    suspend fun register(id: String, password: String, name: String)
}