package com.example.stegomessenger.data.search

interface SearchRepository {
    suspend fun searchByQuery(query: String): List<SearchResult>
}