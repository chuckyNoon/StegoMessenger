package com.example.stegomessenger.compose.repository

import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.search.model.MatchingUser

class MatchingUsersRepositoryImpl(
    private val apiService: ApiService
): MatchingUsersRepository {

    override suspend fun fetchUsersByText(text: String): List<MatchingUser> {
        return apiService.search(text).matchingUsers
    }
}