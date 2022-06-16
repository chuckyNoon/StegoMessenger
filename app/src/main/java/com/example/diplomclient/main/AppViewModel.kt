package com.example.diplomclient.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aita.arch.di.regular.AppDepsProvider
import com.aita.arch.di.regular.DateTimeFormatter
import com.example.diplomclient.arch.flux.dispatcher.Dispatchers
import com.example.diplomclient.arch.flux.util.AppViewModelFactory

class AppViewModel(app: Application) : AndroidViewModel(app) {

    val appViewModelFactory: AppViewModelFactory
    val appDepsProvider: AppDepsProvider

    init {
        val session = Dispatchers.newSession()
        val dispatcher = session.dispatcher

        appDepsProvider = AppDepsProvider(
            dispatcher = dispatcher,
            dateTimeFormatter = DateTimeFormatter(app)
        )
        appViewModelFactory = AppViewModelFactory(app, appDepsProvider)
    }
}
