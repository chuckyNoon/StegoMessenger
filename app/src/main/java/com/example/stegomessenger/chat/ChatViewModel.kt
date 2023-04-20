package com.example.stegomessenger.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel1
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    dispatcher: Dispatcher,
    chatReducer: ChatReducer,
    chatMiddleware: ChatMiddleware
) :
    AbsViewModel1(dispatcher) {

    private val _viewStateLiveData: MutableLiveData<ChatViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ChatViewState> = _viewStateLiveData

    private val _completeEventLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val completeLiveData: LiveData<Unit> = _completeEventLiveData

    init {
        attachManagedStore(
            initialState = ChatState.EMPTY,
            reducer = chatReducer,
            middleware = listOf(chatMiddleware),
        ) { newState: ChatState ->
            _viewStateLiveData.value = newState.viewState
            newState.completeEvent?.readValue()?.let {
                _completeEventLiveData.value = Unit
            }
        }
    }
}
