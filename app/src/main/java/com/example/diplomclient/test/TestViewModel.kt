package com.example.diplomclient.test

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder
import com.example.diplomclient.test.network.TestMiddleware

class TestViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<TestViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<TestViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = TestState.EMPTY,
            reducer = TestReducer(),
            middleware = listOf(
                TestMiddleware(apiHelper)
            ),
        ) { newState: TestState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
