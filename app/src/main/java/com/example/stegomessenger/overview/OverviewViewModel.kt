package com.example.stegomessenger.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel1
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    dispatcher: Dispatcher,
    overviewReducer: OverviewReducer,
    overviewMiddleware: OverviewMiddleware
) :
    AbsViewModel1(dispatcher) {

    private val _viewStateLiveData: MutableLiveData<OverviewViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<OverviewViewState> = _viewStateLiveData

    init {
        attachManagedStore(
            initialState = OverviewState.EMPTY,
            reducer = overviewReducer,
            middleware = listOf(overviewMiddleware),
        ) { newState: OverviewState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
