package com.example.diplomclient.overview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder

class OverviewViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<OverviewViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<OverviewViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = OverviewState.EMPTY,
            reducer = OverviewReducer(appDepsProvider.dateTimeFormatter),
            middleware = listOf(
                OverviewMiddleware(apiHelper)
            ),
        ) { newState: OverviewState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
