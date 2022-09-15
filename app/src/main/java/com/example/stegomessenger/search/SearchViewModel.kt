package com.example.stegomessenger.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.common.network.RetrofitBuilder

class SearchViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<SearchViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<SearchViewState> = _viewStateLiveData

    private val _backLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val backLiveData: LiveData<Unit> = _backLiveData

    init {
        val apiService = appDepsProvider.apiService
        val stringsProvider = appDepsProvider.stringsProvider

        attachManagedStore(
            initialState = SearchState.EMPTY,
            reducer = SearchReducer(),
            middleware = listOf(
                SearchMiddleware(apiService, stringsProvider)
            ),
        ) { newState: SearchState ->
            _viewStateLiveData.value = newState.viewState
            newState.backEvent?.readValue()?.let {
                _backLiveData.value = Unit
            }
        }
    }
}
