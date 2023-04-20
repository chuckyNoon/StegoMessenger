package com.example.stegomessenger.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stegomessenger.arch.infra.AbsViewModel
import com.example.stegomessenger.arch.redux.dispatcher.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    dispatcher: Dispatcher,
    loginMiddleware: LoginMiddleware,
    loginReducer: LoginReducer
) :
    AbsViewModel(dispatcher) {

    private val _viewStateLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    val viewStateLiveData: LiveData<LoginViewState> = _viewStateLiveData

    init {
        attachManagedStore(
            initialState = LoginState.EMPTY,
            reducer = loginReducer,
            middleware = listOf(loginMiddleware),
        ) { newState: LoginState ->
            _viewStateLiveData.value = newState.viewState
        }
    }
}
