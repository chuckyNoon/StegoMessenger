package com.example.stegomessenger.v2.compose.repository

import com.example.stegomessenger.v2.compose.model.MatchingUser

interface MatchingUsersRepository{
    suspend fun fetchUsersByText(text: String) : List<MatchingUser>
}