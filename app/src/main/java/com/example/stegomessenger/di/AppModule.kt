package com.example.stegomessenger.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchers
import com.example.stegomessenger.arch.util.*
import com.example.stegomessenger.common.Config
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.common.network.FakeApiServiceImpl
import com.example.stegomessenger.main.MainApplication
import com.example.stegomessenger.main.SyncHelper
import com.google.gson.Gson
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
    fun gson() = GsonBuilder().setLenient().create()

    @Provides
    fun apiService(prefs: Prefs, gson: Gson): ApiService = if (!Config.IS_SERVER_ENABLED) {
        FakeApiServiceImpl()
    } else {
        val timeOutSeconds = 15L

        val httpClient = OkHttpClient.Builder()
            .readTimeout(timeOutSeconds, TimeUnit.SECONDS)
            .connectTimeout(timeOutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeOutSeconds, TimeUnit.SECONDS)
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

        Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

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