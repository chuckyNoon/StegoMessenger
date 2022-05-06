package com.example.diplomclient.arch.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://e57c7c0c815bae.lhrtunnel.link/api/"

    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build() // Doesn't require the adapter
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}
