package com.example.diplomclient.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.SingleEventLiveData
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder

class ChatViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<ChatViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ChatViewState> = _viewStateLiveData

    private val _completeEventLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val completeLiveData: LiveData<Unit> = _completeEventLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = ChatState.EMPTY,
            reducer = ChatReducer(appDepsProvider.dateTimeFormatter),
            middleware = listOf(
                ChatMiddleware()
            ),
        ) { newState: ChatState ->
            _viewStateLiveData.value = newState.viewState
            newState.completeEvent?.readValue()?.let {
                _completeEventLiveData.value = Unit
            }
        }
    }
}
