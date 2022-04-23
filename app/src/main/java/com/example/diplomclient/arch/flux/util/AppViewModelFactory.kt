package com.example.diplomclient.arch.flux.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aita.arch.di.regular.AppDepsProvider

class AppViewModelFactory(
    private val app: Application,
    private val appDepsProvider: AppDepsProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        try {
            modelClass
                .getConstructor(Application::class.java, AppDepsProvider::class.java)
                .newInstance(app, appDepsProvider)
        } catch (e: Exception) {
            throw IllegalArgumentException(
                "Failed to instantiate $modelClass with expected constructor(Application, AppDepsProvider)",
                e
            )
        }
}