package com.example.diplomclient.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder

class SearchViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<SearchViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<SearchViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = SearchState.EMPTY,
            reducer = SearchReducer(),
            middleware = listOf(
                SearchMiddleware(apiHelper)
            ),
        ) { newState: SearchState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
