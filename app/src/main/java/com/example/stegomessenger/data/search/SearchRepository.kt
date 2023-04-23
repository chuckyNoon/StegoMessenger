package com.example.stegomessenger.data.search

import com.example.stegomessenger.common.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun searchByQuery(query: String): List<SearchResult> = withContext(Dispatchers.IO){
        apiService.search(query).searchResults
    }
}