package com.aita.arch.di.regular

import com.example.diplomclient.arch.flux.dispatcher.Dispatcher

class AppDepsProvider(
    val dispatcher: Dispatcher,
    val dateTimeFormatter: DateTimeFormatter
)
