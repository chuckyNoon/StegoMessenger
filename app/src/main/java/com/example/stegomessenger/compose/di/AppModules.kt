package com.example.stegomessenger.compose.di

import android.content.Context
import com.example.stegomessenger.arch.util.*
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.common.network.FakeApiService
import com.example.stegomessenger.compose.repository.*
import com.example.stegomessenger.main.MainApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModules {

    @Provides
    fun context(): Context = MainApplication.getInstance()

    @Provides
    fun apiService(): ApiService = FakeApiService()

    @Provides
    fun stringsProvider(context: Context): StringsProvider = DefaultStringsProvider(context)

    @Provides
    fun sharedPreferences(context: Context): Prefs = DefaultPrefs(context)

    @Provides
    fun dateTimeFormatter(): DateTimeFormatter = DefaultDateTimeFormatter()

    @Provides
    fun chatsRepository(apiService: ApiService): ChatsRepository = ChatsRepositoryImpl(apiService)

    @Provides
    fun matchingUsersRepository(apiService: ApiService): MatchingUsersRepository =
        MatchingUsersRepositoryImpl(apiService)

    @Provides
    fun usersRepository(apiService: ApiService): UserRepository =
        UserRepository(apiService)
}