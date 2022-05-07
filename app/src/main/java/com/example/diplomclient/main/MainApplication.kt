package com.example.diplomclient.main

import android.app.Application

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
