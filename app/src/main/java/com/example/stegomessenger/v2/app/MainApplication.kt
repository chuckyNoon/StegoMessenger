package com.example.stegomessenger.v2.app

import android.app.Application
import android.content.Context
import com.example.stegomessenger.v2.core.infra.*
import com.example.stegomessenger.v2.core.network.ApiService
import com.example.stegomessenger.v2.core.network.FakeApiService
import com.example.stegomessenger.v2.data.chat.ChatsRepository
import com.example.stegomessenger.v2.data.chat.ChatsRepositoryImpl
import com.example.stegomessenger.v2.data.matching_user.MatchingUsersRepository
import com.example.stegomessenger.v2.data.matching_user.MatchingUsersRepositoryImpl
import com.example.stegomessenger.v2.data.user.UserRepository
import com.example.stegomessenger.v2.features.chat.NewChatViewModel
import com.example.stegomessenger.v2.features.login.NewLoginViewModel
import com.example.stegomessenger.v2.features.overview.NewOverviewViewModel
import com.example.stegomessenger.v2.features.search.NewSearchViewModel
import com.example.stegomessenger.v2.features.sign_up.NewSignUpViewModel
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    androidModule,
                    dataModule
                )
            )
        }
    }

    companion object {
        private var INSTANCE: Application? = null

        fun getInstance(): Application = requireNotNull(INSTANCE) { "shouldn't happen" }
    }
}


val androidModule = module {
    single { FakeApiService() as ApiService }
    single { DefaultStringsProvider(androidContext()) as StringsProvider }
    single { DefaultPrefs(androidContext()) as Prefs }
    single { DefaultDateTimeFormatter() as DateTimeFormatter }
    viewModel { NewLoginViewModel(get(), get(), get()) }
    viewModel { NewChatViewModel(get(), get(), get(), get()) }
    viewModel { NewOverviewViewModel(get(), get(), get()) }
    viewModel { NewSignUpViewModel(get(), get()) }
    viewModel { NewSearchViewModel(get()) }
}

val dataModule = module {
    single { ChatsRepositoryImpl(get()) as ChatsRepository }
    single { MatchingUsersRepositoryImpl(get()) as MatchingUsersRepository}
    single { UserRepository(get()) }
}