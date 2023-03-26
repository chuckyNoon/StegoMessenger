package com.example.stegomessenger.v2.data.matching_user

import com.example.stegomessenger.v2.common.model.MatchingUser

interface MatchingUsersRepository{
    suspend fun fetchUsersByText(text: String) : List<MatchingUser>
}