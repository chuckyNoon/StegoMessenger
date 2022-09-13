package com.example.diplomclient.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: get rid of it, b.c. it leaks
fun launchBackgroundWork(runnable: suspend () -> Unit) =
    GlobalScope.launch(Dispatchers.Default) {
        try {
            runnable()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
