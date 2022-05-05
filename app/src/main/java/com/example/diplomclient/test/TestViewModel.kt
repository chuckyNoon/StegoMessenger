package com.example.diplomclient.test

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TestViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<TestViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<TestViewState> = _viewStateLiveData

    private val apiHelper = ApiHelper(RetrofitBuilder.apiService)

    init {
        Log.d("ff", "start1")
        attachManagedStore(
            initialState = TestState.EMPTY,
            reducer = TestReducer(),
            middleware = emptyList(),
        ) { newState: TestState ->
            _viewStateLiveData.value = newState.viewState
        }

        try {
            viewModelScope.launch(Dispatchers.Default) {
                initRequest()
            }
        } catch (e: Exception) {
        }
    }

    private suspend fun initRequest() {
        try {
            val resp = apiHelper.getResponse()
            Log.d("ff", resp.value)
        } catch (e: Exception) {
            Log.d("ff", e.message.toString())
        }
    }
}
