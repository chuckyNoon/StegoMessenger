package com.example.stegomessenger.arch.util

import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher

class AppDepsProvider(
    val dispatcher: Dispatcher,
    val dateTimeFormatter: DateTimeFormatter,
    val stringsProvider: StringsProvider
)
