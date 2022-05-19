package com.example.diplomclient.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel

class ResultViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<ResultViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ResultViewState> = _viewStateLiveData

    init {
        attachManagedStore(
            initialState = ResultState.EMPTY,
            reducer = ResultReducer(),
            middleware = listOf(
                ResultMiddleware()
            ),
        ) { newState: ResultState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
