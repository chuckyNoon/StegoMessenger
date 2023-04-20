package com.example.stegomessenger.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel1
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    dispatcher: Dispatcher,
    searchMiddleware: SearchMiddleware,
    searchReducer: SearchReducer
) :
    AbsViewModel1(dispatcher) {

    private val _viewStateLiveData: MutableLiveData<SearchViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<SearchViewState> = _viewStateLiveData

    private val _backLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val backLiveData: LiveData<Unit> = _backLiveData

    init {
        attachManagedStore(
            initialState = SearchState.EMPTY,
            reducer = searchReducer,
            middleware = listOf(searchMiddleware),
        ) { newState: SearchState ->
            _viewStateLiveData.value = newState.viewState
            newState.backEvent?.readValue()?.let {
                _backLiveData.value = Unit
            }
        }
    }
}
