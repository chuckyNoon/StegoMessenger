package com.example.diplomclient.main

import android.app.Application
import androidx.lifecycle.LiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.SingleEventLiveData
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.test.TestAction

class MainViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _navigationLiveData = SingleEventLiveData<MainNavigation>()
    val navigationLiveData: LiveData<MainNavigation> = _navigationLiveData

    init {
        attachManagedStore(
            initialState = MainState.EMPTY,
            reducer = MainReducer(),
            middleware = emptyList(),
        ) { newState: MainState ->
            newState.navigationEvent?.readValue()?.let {
                _navigationLiveData.value = it
            }
        }

        dispatch(MainAction.ShowTestFragment)
        dispatch(TestAction.Init)
    }
}
