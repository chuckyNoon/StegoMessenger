package com.example.stegomessenger.stego_dialog

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.common.network.RetrofitBuilder
import com.example.stegomessenger.main.MainApplication

class StegoViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<StegoViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<StegoViewState> = _viewStateLiveData

    private val _closeLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val closeLiveData: LiveData<Unit> = _closeLiveData

    init {
        val apiService = appDepsProvider.apiService
        val stringsProvider = appDepsProvider.stringsProvider
        val context = MainApplication.getInstance()

        attachManagedStore(
            initialState = StegoState.EMPTY,
            reducer = StegoReducer(stringsProvider),
            middleware = listOf(
                StegoMiddleware(context, apiService, stringsProvider)
            ),
        ) { newState: StegoState ->
            _viewStateLiveData.value = newState.viewState
            newState.closeEvent?.readValue()?.let {
                _closeLiveData.value = Unit
            }
        }
    }
}
