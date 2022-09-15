package com.example.stegomessenger.arch.util

import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import com.example.stegomessenger.common.network.ApiService

class AppDepsProvider(
    val dispatcher: Dispatcher,
    val dateTimeFormatter: DateTimeFormatter,
    val stringsProvider: StringsProvider,
    val apiService: ApiService,
    val prefs: Prefs
)
