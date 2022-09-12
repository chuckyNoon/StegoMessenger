package com.example.diplomclient.arch.util

import com.example.diplomclient.arch.redux.dispatcher.Dispatcher

class AppDepsProvider(
    val dispatcher: Dispatcher,
    val dateTimeFormatter: DateTimeFormatter
)
