package com.example.diplomclient.registration

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder

class RegistrationViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<RegistrationViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<RegistrationViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = RegistrationState.EMPTY,
            reducer = RegistrationReducer(),
            middleware = listOf(
                RegistrationMiddleware(apiHelper)
            ),
        ) { newState: RegistrationState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}