package com.example.stegomessenger.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.infra.AbsViewModel

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
