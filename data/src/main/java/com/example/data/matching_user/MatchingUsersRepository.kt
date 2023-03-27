package com.example.data.matching_user

interface MatchingUsersRepository{
    suspend fun fetchUsersByText(text: String) : List<MatchingUser>
}