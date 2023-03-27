package com.example.stegomessenger.v2.app

import android.app.Application
import com.example.core.infra.*
import com.example.core.network.ApiService
import com.example.core.network.FakeApiService
import com.example.data.chat.ChatsRepository
import com.example.data.chat.ChatsRepositoryImpl
import com.example.data.matching_user.MatchingUsersRepository
import com.example.data.matching_user.MatchingUsersRepositoryImpl
import com.example.data.user.UserRepository
import com.example.features.chat.NewChatViewModel
import com.example.features.login.NewLoginViewModel
import com.example.features.overview.NewOverviewViewModel
import com.example.features.search.NewSearchViewModel
import com.example.features.sign_up.NewSignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    coreModule,
                    featureModule,
                    dataModule
                )
            )
        }
    }
}


val coreModule = module {
    single { FakeApiService() as ApiService }
    single { DefaultStringsProvider(androidContext()) as StringsProvider }
    single { DefaultPrefs(androidContext()) as Prefs }
    single { DefaultDateTimeFormatter() as DateTimeFormatter }
}

val featureModule = module {
    viewModel { NewLoginViewModel(get(), get(), get()) }
    viewModel { NewChatViewModel(get(), get(), get(), get()) }
    viewModel { NewOverviewViewModel(get(), get(), get()) }
    viewModel { NewSignUpViewModel(get(), get()) }
    viewModel { NewSearchViewModel(get()) }
}

val dataModule = module {
    single { ChatsRepositoryImpl(get()) as ChatsRepository }
    single { MatchingUsersRepositoryImpl(get()) as MatchingUsersRepository }
    single { UserRepository(get()) }
}