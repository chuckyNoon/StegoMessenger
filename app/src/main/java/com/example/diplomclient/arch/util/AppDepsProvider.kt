package com.aita.arch.di.regular

import com.aita.arch.dispatcher.Dispatcher

class AppDepsProvider(
    val dispatcher: Dispatcher,
    val stringsProvider: StringsProvider,
    val dateTimeFormatter: DateTimeFormatter
)
