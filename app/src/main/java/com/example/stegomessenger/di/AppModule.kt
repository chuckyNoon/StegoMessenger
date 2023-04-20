package com.example.stegomessenger.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchers
import com.example.stegomessenger.arch.util.*
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.common.network.FakeApiServiceImpl
import com.example.stegomessenger.main.MainApplication
import com.example.stegomessenger.main.SyncHelper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun context(): Context = MainApplication.getInstance()

    @Provides
    fun stringsProvider(context: Context): StringsProvider = DefaultStringsProvider(context)

    @Provides
    fun prefs(context: Context): Prefs = DefaultPrefs(context)

    @Provides
    fun apiService(prefs: Prefs): ApiService = FakeApiServiceImpl()
    /*
    @Provides
    fun apiService(prefs: Prefs): ApiService {
        val BASE_URL = "https://c8f5-178-155-4-69.eu.ngrok.io/stego/api/"
        val TIME_OUT_SECONDS = 15L

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val httpClient = OkHttpClient.Builder()
            .readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val token = prefs.getString(PrefsContract.TOKEN, null)
                val request = chain
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
            .build()
            .create(ApiService::class.java)
    }*/

    @Provides
    @Singleton
    fun dispatcher(): Dispatcher = Dispatchers.newSession().dispatcher

    @Provides
    fun requestManager(context: Context): RequestManager = Glide.with(context)

    @Provides
    fun syncHelper(prefs: Prefs): SyncHelper = SyncHelper(prefs)

    @Provides
    fun dateTimeFormatter(): DateTimeFormatter = DefaultDateTimeFormatter()

}