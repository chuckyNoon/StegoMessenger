package com.example.diplomclient.arch.network

import android.util.Log
import com.example.diplomclient.common.PrefsContract
import com.example.diplomclient.common.PrefsHelper
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private const val BASE_URL = "https://58b3-79-104-7-120.eu.ngrok.io/api/"

    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Log.d("intercepter", "here1")

        val prefs = PrefsHelper.getPrefs()

        val httpClient = OkHttpClient.Builder()
            .readTimeout(600, TimeUnit.SECONDS)
            .connectTimeout(600, TimeUnit.SECONDS)
            .writeTimeout(600, TimeUnit.SECONDS)
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
