package com.example.stegomessenger.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel1
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import javax.inject.Inject

class ResultViewModel @Inject constructor(dispatcher: Dispatcher) :
    AbsViewModel1(dispatcher) {

    private val _viewStateLiveData: MutableLiveData<ResultViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<ResultViewState> = _viewStateLiveData

    init {
        attachManagedStore(
            initialState = ResultState.EMPTY,
            reducer = ResultReducer(),
            middleware = listOf(
                ResultMiddleware()
            ),
        ) { newState: ResultState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
