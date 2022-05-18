package com.example.diplomclient.stego_dialog

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder

class StegoViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<StegoViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<StegoViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = StegoState.EMPTY,
            reducer = StegoReducer(),
            middleware = listOf(
                StegoMiddleware(apiHelper)
            ),
        ) { newState: StegoState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
