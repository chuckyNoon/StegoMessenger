package com.example.stegomessenger.features.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    dispatcher: Dispatcher,
    overviewReducer: OverviewReducer,
    overviewMiddleware: OverviewMiddleware
) :
    AbsViewModel(dispatcher) {

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
