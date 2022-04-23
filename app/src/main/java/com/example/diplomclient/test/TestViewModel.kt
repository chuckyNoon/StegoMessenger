package com.example.diplomclient.test

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel

class TestViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<TestViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<TestViewState> = _viewStateLiveData

    init {
        attachManagedStore(
            initialState = TestState.EMPTY,
            reducer = TestReducer(),
            middleware = emptyList(),
        ) { newState: TestState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
