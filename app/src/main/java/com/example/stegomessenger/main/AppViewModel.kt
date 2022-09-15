package com.example.stegomessenger.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.util.DefaultDateTimeFormatter
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchers
import com.example.stegomessenger.arch.redux.util.AppViewModelFactory
import com.example.stegomessenger.arch.util.DefaultPrefs
import com.example.stegomessenger.arch.util.DefaultStringsProvider
import com.example.stegomessenger.common.network.RetrofitBuilder

class AppViewModel(app: Application) : AndroidViewModel(app) {

    val appViewModelFactory: AppViewModelFactory
    val appDepsProvider: AppDepsProvider

    init {
        val session = Dispatchers.newSession()
        val dispatcher = session.dispatcher
        val context = MainApplication.getInstance()
        val prefs = DefaultPrefs(context)

        appDepsProvider = AppDepsProvider(
            dispatcher = dispatcher,
            dateTimeFormatter = DefaultDateTimeFormatter(),
            stringsProvider = DefaultStringsProvider(context),
            apiService = RetrofitBuilder(prefs).apiService,
            prefs = prefs
        )
        appViewModelFactory = AppViewModelFactory(app, appDepsProvider)
    }
}
