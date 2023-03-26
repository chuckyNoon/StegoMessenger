package com.example.stegomessenger.v2.data.matching_user

interface MatchingUsersRepository{
    suspend fun fetchUsersByText(text: String) : List<MatchingUser>
}