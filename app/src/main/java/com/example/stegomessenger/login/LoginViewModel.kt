package com.example.stegomessenger.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.infra.AbsViewModel

class LoginViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<LoginViewState> = _viewStateLiveData

    init {
        val apiService = appDepsProvider.apiService
        val stringsProvider = appDepsProvider.stringsProvider
        val prefs = appDepsProvider.prefs

        attachManagedStore(
            initialState = LoginState.EMPTY,
            reducer = LoginReducer(),
            middleware = listOf(
                LoginMiddleware(apiService, prefs, stringsProvider)
            ),
        ) { newState: LoginState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
