package com.example.diplomclient.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.infra.AbsViewModel
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.RetrofitBuilder

class ChatViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<ChatViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ChatViewState> = _viewStateLiveData

    init {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)

        attachManagedStore(
            initialState = ChatState.EMPTY,
            reducer = ChatReducer(),
            middleware = listOf(
                ChatMiddleware(apiHelper)
            ),
        ) { newState: ChatState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}