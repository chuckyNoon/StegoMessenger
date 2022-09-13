package com.example.stegomessenger.stego_dialog

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.arch.network.ApiHelper
import com.example.stegomessenger.arch.network.RetrofitBuilder
import com.example.stegomessenger.main.MainApplication

class StegoViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<StegoViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<StegoViewState> = _viewStateLiveData

    private val _closeLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val closeLiveData: LiveData<Unit> = _closeLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)
        val context = MainApplication.getInstance()
        val stringsProvider = appDepsProvider.stringsProvider

        attachManagedStore(
            initialState = StegoState.EMPTY,
            reducer = StegoReducer(stringsProvider),
            middleware = listOf(
                StegoMiddleware(apiHelper, context, stringsProvider)
            ),
        ) { newState: StegoState ->
            _viewStateLiveData.value = newState.viewState
            newState.closeEvent?.readValue()?.let {
                _closeLiveData.value = Unit
            }
        }
    }
}
