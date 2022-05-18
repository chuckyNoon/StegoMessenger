package com.example.diplomclient.main.navigation

import android.app.Application
import androidx.lifecycle.LiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.SingleEventLiveData
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder
import com.example.diplomclient.common.PrefsContract
import com.example.diplomclient.common.PrefsHelper
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.main.SyncHelper
import kotlinx.coroutines.delay

class CoreNavViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _navigationLiveData = SingleEventLiveData<CoreNav>()
    val navigationLiveData: LiveData<CoreNav> = _navigationLiveData

    private val _errorLiveData = SingleEventLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    init {
        val apiService = ApiHelper(RetrofitBuilder.apiService)
        attachManagedStore(
            initialState = CoreNavState.EMPTY,
            reducer = CoreNavReducer(),
            middleware = listOf(
                CoreMiddleware(
                    apiHelper = apiService,
                    syncHelper = SyncHelper(prefs = PrefsHelper.getPrefs())
                )
            ),
        ) { newState: CoreNavState ->
            newState.navigationEvent?.readValue()?.let {
                _navigationLiveData.value = it
            }
            newState.errorEvent?.readValue()?.let {
                _errorLiveData.value = it
            }
        }
        dispatch(CoreAction.ReloadChats)
        launchBackgroundWork {
            delay(5000)
            dispatch(CoreAction.SyncWithServer)
        }
        performInitialNavigation()
    }

    private fun performInitialNavigation() {
        val savedToken = PrefsHelper.getPrefs().getString(PrefsContract.TOKEN, null)
        if (savedToken == null) {
            dispatch(CoreAction.ShowLogin)
        } else {
            dispatch(CoreAction.ShowOverviewFragment)
        }
    }
}
