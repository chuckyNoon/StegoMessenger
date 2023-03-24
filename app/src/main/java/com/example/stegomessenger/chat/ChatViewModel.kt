package com.example.stegomessenger.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.stegomessenger.arch.util.AppDepsProvider
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.main.MainApplication

class ChatViewModel(app: Application, appDepsProvider: AppDepsProvider) :
    AbsViewModel(app, appDepsProvider) {

    private val _viewStateLiveData: MutableLiveData<ChatViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ChatViewState> = _viewStateLiveData

    private val _completeEventLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val completeLiveData: LiveData<Unit> = _completeEventLiveData

    init {
        val requestManager = Glide.with(MainApplication.getInstance())

        attachManagedStore(
            initialState = ChatState.INITIAL,
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
