package com.example.diplomclient.main.navigation

import android.app.Application
import androidx.lifecycle.LiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.SingleEventLiveData
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.common.PrefsContract
import com.example.diplomclient.common.PrefsHelper

class CoreNavViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _navigationLiveData = SingleEventLiveData<CoreNav>()
    val navigationLiveData: LiveData<CoreNav> = _navigationLiveData

    private val _errorLiveData = SingleEventLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    init {
        attachManagedStore(
            initialState = CoreNavState.EMPTY,
            reducer = CoreNavReducer(),
            middleware = emptyList(),
        ) { newState: CoreNavState ->
            newState.navigationEvent?.readValue()?.let {
                _navigationLiveData.value = it
            }
            newState.errorEvent?.readValue()?.let {
                _errorLiveData.value = it
            }
        }

        dispatch(CoreNavAction.ShowOverviewFragment)
        // performInitialNavigation()
    }

    private fun performInitialNavigation() {
        val savedToken = PrefsHelper.getPrefs().getString(PrefsContract.TOKEN, null)
        if (savedToken == null) {
            dispatch(CoreNavAction.ShowLogin)
        } else {
            dispatch(CoreNavAction.ShowLogin)
        }
    }
}
