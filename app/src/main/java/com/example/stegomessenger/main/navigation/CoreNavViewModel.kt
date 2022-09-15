package com.example.stegomessenger.main.navigation

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.launchBackgroundWork
import com.example.stegomessenger.main.SyncHelper
import kotlinx.coroutines.delay

class CoreNavViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _navigationLiveData = SingleEventLiveData<CoreNav>()
    val navigationLiveData: LiveData<CoreNav> = _navigationLiveData

    private val _errorLiveData = SingleEventLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    init {
        val apiService = appDepsProvider.apiService
        val prefs = appDepsProvider.prefs

        attachManagedStore(
            initialState = CoreNavState.EMPTY,
            reducer = CoreNavReducer(),
            middleware = listOf(
                CoreMiddleware(
                    apiService = apiService,
                    syncHelper = SyncHelper(prefs)
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
        launchBackgroundWork {
            dispatch(CoreAction.ReloadChats)
            delay(5000) // TODO: delete
            dispatch(CoreAction.SyncWithServer)
        }
        performInitialNavigation(prefs)
    }

    private fun performInitialNavigation(prefs: Prefs) {
        val savedToken = prefs.getString(PrefsContract.TOKEN, null)
        if (savedToken == null) {
            dispatch(CoreAction.ShowLogin)
        } else {
            dispatch(CoreAction.ShowOverviewFragment)
        }
    }
}
