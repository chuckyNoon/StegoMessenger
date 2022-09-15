package com.example.stegomessenger.overview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.common.network.RetrofitBuilder

class OverviewViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<OverviewViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<OverviewViewState> = _viewStateLiveData

    init {
        val dateTimeFormatter = appDepsProvider.dateTimeFormatter
        val stringsProvider = appDepsProvider.stringsProvider

        attachManagedStore(
            initialState = OverviewState.EMPTY,
            reducer = OverviewReducer(
                dateTimeFormatter,
                stringsProvider
            ),
            middleware = listOf(
                OverviewMiddleware()
            ),
        ) { newState: OverviewState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
