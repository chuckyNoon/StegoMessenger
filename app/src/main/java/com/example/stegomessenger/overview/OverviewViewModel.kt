package com.example.stegomessenger.overview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.common.network.ApiHelper
import com.example.stegomessenger.common.network.RetrofitBuilder

class OverviewViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<OverviewViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<OverviewViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = OverviewState.EMPTY,
            reducer = OverviewReducer(
                appDepsProvider.dateTimeFormatter,
                appDepsProvider.stringsProvider
            ),
            middleware = listOf(
                OverviewMiddleware(apiHelper)
            ),
        ) { newState: OverviewState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
