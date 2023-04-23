package com.example.stegomessenger.data.search

import com.example.stegomessenger.common.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun searchByQuery(query: String): List<SearchResult> =
        withContext(Dispatchers.IO) {
            apiService.search(query).searchResults
        }
}