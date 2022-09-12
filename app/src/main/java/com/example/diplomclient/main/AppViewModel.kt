package com.example.diplomclient.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.diplomclient.arch.util.AppDepsProvider
import com.example.diplomclient.arch.util.DateTimeFormatter
import com.example.diplomclient.arch.redux.dispatcher.Dispatchers
import com.example.diplomclient.arch.redux.util.AppViewModelFactory

class AppViewModel(app: Application) : AndroidViewModel(app) {

    val appViewModelFactory: AppViewModelFactory
    val appDepsProvider: AppDepsProvider

    init {
        val session = Dispatchers.newSession()
        val dispatcher = session.dispatcher

        appDepsProvider = AppDepsProvider(
            dispatcher = dispatcher,
            dateTimeFormatter = DateTimeFormatter()
        )
        appViewModelFactory = AppViewModelFactory(app, appDepsProvider)
    }
}
