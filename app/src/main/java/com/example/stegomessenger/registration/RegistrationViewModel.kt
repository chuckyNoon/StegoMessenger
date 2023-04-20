package com.example.stegomessenger.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel1
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    dispatcher: Dispatcher,
    registrationMiddleware: RegistrationMiddleware,
    registrationReducer: RegistrationReducer
) :
    AbsViewModel1(dispatcher) {

    private val _viewStateLiveData: MutableLiveData<RegistrationViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<RegistrationViewState> = _viewStateLiveData

    init {
        attachManagedStore(
            initialState = RegistrationState.EMPTY,
            reducer = registrationReducer,
            middleware = listOf(
                registrationMiddleware
            ),
        ) { newState: RegistrationState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
