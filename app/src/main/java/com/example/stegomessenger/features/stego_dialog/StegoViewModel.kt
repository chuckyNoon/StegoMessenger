package com.example.stegomessenger.features.stego_dialog


import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.example.stegomessenger.arch.SingleEventLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@HiltViewModel
class StegoViewModel @Inject constructor(
    dispatcher: Dispatcher,
    stegoReducer: StegoReducer,
    stegoMiddleware: StegoMiddleware
) :
    AbsViewModel(dispatcher) {

    private val _viewStateLiveData: MutableLiveData<StegoViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<StegoViewState> = _viewStateLiveData

    private val _closeLiveData: SingleEventLiveData<Unit> = SingleEventLiveData()
    val closeLiveData: LiveData<Unit> = _closeLiveData

    init {
        attachManagedStore(
            initialState = StegoState.EMPTY,
            reducer = stegoReducer,
            middleware = listOf(stegoMiddleware),
        ) { newState: StegoState ->
            _viewStateLiveData.value = newState.viewState
            newState.closeEvent?.readValue()?.let {
                _closeLiveData.value = Unit
            }
        }
    }
}
