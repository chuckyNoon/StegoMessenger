package com.example.stegomessenger.registration

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.infra.AbsViewModel

class RegistrationViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<RegistrationViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<RegistrationViewState> = _viewStateLiveData

    init {
        val apiService = appDepsProvider.apiService
        val prefs = appDepsProvider.prefs

        attachManagedStore(
            initialState = RegistrationState.EMPTY,
            reducer = RegistrationReducer(),
            middleware = listOf(
                RegistrationMiddleware(apiService, prefs)
            ),
        ) { newState: RegistrationState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
