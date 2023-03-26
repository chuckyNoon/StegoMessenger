package com.example.stegomessenger.v2.data.matching_user

import com.example.stegomessenger.v2.core.network.ApiService

class MatchingUsersRepositoryImpl(
    private val apiService: ApiService
): MatchingUsersRepository {

    override suspend fun fetchUsersByText(text: String): List<MatchingUser> {
        return apiService.search(text).matchingUsers
    }
}