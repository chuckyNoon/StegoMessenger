package com.example.stegomessenger.features.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import javax.inject.Inject

class ResultViewModel @Inject constructor(dispatcher: Dispatcher) :
    AbsViewModel(dispatcher) {

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
