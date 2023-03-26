package com.example.stegomessenger.v2.compose.repository

import com.example.stegomessenger.v2.common.network.ApiService
import com.example.stegomessenger.v2.compose.model.MatchingUser

class MatchingUsersRepositoryImpl(
    private val apiService: ApiService
): MatchingUsersRepository {

    override suspend fun fetchUsersByText(text: String): List<MatchingUser> {
        return apiService.search(text).matchingUsers
    }
}