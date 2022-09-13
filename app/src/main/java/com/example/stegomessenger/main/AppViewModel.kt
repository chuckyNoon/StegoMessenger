package com.example.stegomessenger.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.util.DateTimeFormatter
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchers
import com.example.stegomessenger.arch.redux.util.AppViewModelFactory
import com.example.stegomessenger.arch.util.StringsProvider

class AppViewModel(app: Application) : AndroidViewModel(app) {

    val appViewModelFactory: AppViewModelFactory
    val appDepsProvider: AppDepsProvider

    init {
        val session = Dispatchers.newSession()
        val dispatcher = session.dispatcher
        val context = MainApplication.getInstance()

        appDepsProvider = AppDepsProvider(
            dispatcher = dispatcher,
            dateTimeFormatter = DateTimeFormatter(),
            stringsProvider = StringsProvider(context)
        )
        appViewModelFactory = AppViewModelFactory(app, appDepsProvider)
    }
}
