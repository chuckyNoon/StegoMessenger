package com.example.core

import android.util.Log

object AppLogger {
    fun log(text: String) =
        Log.d("AppLogger", text)
}
