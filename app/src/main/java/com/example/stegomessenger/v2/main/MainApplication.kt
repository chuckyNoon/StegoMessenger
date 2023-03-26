package com.example.stegomessenger.v2.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }

    companion object {
        private var INSTANCE: Application? = null

        fun getInstance(): Application = requireNotNull(INSTANCE) { "shouldn't happen" }
    }
}
