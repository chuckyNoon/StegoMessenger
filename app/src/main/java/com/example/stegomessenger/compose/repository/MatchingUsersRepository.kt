package com.example.stegomessenger.compose.repository

import com.example.stegomessenger.search.model.MatchingUser

interface MatchingUsersRepository{
    suspend fun fetchUsersByText(text: String) : List<MatchingUser>
}