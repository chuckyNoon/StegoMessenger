package com.example.stegomessenger.content_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher

class ContentViewModel(
    dispatcher: Dispatcher,
    contentReducer: ContentReducer,
    contentMiddleware: ContentMiddleware
) :
    AbsViewModel(dispatcher) {

    private val _viewStateLiveData: MutableLiveData<ContentViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ContentViewState> = _viewStateLiveData

    init {
        attachManagedStore(
            initialState = ContentState.EMPTY,
            reducer = contentReducer,
            middleware = listOf(contentMiddleware),
        ) { newState: ContentState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}