package com.example.diplomclient.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun launchBackgroundWork(runnable: suspend () -> Unit) =
    GlobalScope.launch(Dispatchers.Main) {
        runnable()
    }
