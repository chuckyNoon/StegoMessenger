package com.example.diplomclient.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder

class LoginViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<LoginViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = LoginState.EMPTY,
            reducer = LoginReducer(),
            middleware = listOf(
                LoginMiddleware(apiHelper)
            ),
        ) { newState: LoginState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
