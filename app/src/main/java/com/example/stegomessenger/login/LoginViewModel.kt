package com.example.stegomessenger.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.common.network.ApiHelper
import com.example.stegomessenger.common.network.RetrofitBuilder
import com.example.stegomessenger.common.PrefsHelper

class LoginViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<LoginViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)
        val prefsEditor = PrefsHelper.getEditor()
        val stringsProvider = appDepsProvider.stringsProvider

        attachManagedStore(
            initialState = LoginState.EMPTY,
            reducer = LoginReducer(),
            middleware = listOf(
                LoginMiddleware(apiHelper, prefsEditor, stringsProvider)
            ),
        ) { newState: LoginState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
