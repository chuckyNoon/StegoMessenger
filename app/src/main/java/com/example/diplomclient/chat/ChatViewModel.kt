package com.example.diplomclient.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.diplomclient.arch.util.AppDepsProvider
import com.example.diplomclient.arch.SingleEventLiveData
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder
import com.example.diplomclient.main.MainApplication

class ChatViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<ChatViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ChatViewState> = _viewStateLiveData

    private val _completeEventLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val completeLiveData: LiveData<Unit> = _completeEventLiveData

    init {
        val requestManager = Glide.with(MainApplication.getInstance())

        attachManagedStore(
            initialState = ChatState.EMPTY,
            reducer = ChatReducer(appDepsProvider.dateTimeFormatter),
            middleware = listOf(
                ChatMiddleware(requestManager)
            ),
        ) { newState: ChatState ->
            _viewStateLiveData.value = newState.viewState
            newState.completeEvent?.readValue()?.let {
                _completeEventLiveData.value = Unit
            }
        }
    }
}
