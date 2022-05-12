package com.example.diplomclient.arch.network

import android.util.Log
import com.example.diplomclient.common.PrefsContract
import com.example.diplomclient.common.PrefsHelper
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://796c0c1ed548dd.lhrtunnel.link/api/"

    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Log.d("intercepter", "here1")

        val prefs = PrefsHelper.getPrefs()

        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = prefs.getString(PrefsContract.TOKEN, null)
                Log.d("intercepter", "here2 $token ")
                val request: Request = chain
                    .request()
                    .newBuilder()
                    .addHeader("token", token ?: "no token")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build() // Doesn't require the adapter
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}
