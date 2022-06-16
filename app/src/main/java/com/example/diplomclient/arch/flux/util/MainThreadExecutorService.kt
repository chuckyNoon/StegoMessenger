package com.example.diplomclient.arch.flux.util

import android.os.Build
import android.os.Handler
import android.os.Looper
import com.example.diplomclient.arch.flux.util.AbsDummyExecutorService

internal class MainThreadExecutorService : AbsDummyExecutorService() {

    private val mainThreadHandler =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Handler.createAsync(Looper.getMainLooper())
        } else {
            Handler(Looper.getMainLooper())
        }

    override fun execute(command: Runnable) {
        mainThreadHandler.post(command)
    }
}
