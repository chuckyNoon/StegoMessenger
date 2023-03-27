package com.example.features.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.infra.StringsProvider
import com.example.data.user.UserRepository
import com.example.core.arch.IntentHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewSignUpViewModel(
    val stringsProvider: StringsProvider,
    val userRepository: UserRepository
) : ViewModel(), IntentHandler<SignUpIntent> {

    private var state = RegistrationState.INITIAL
    var viewStateLiveData = MutableLiveData(RegistrationViewState.INITIAL)

    override fun obtainIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.OnRegisterClick -> {
                state = state.copy(isLoading = true)
                rebuildViewState()

                viewModelScope.launch {
                    delay(4000)
                    userRepository.addUser(
                        login = intent.login,
                        password = intent.password,
                        name = intent.name
                    )
                    state = state.copy(isLoading = false)
                    rebuildViewState()
                }
            }
        }
    }


    private fun rebuildViewState() {
        val oldState = state

        val viewState = RegistrationViewState(
            isLoading = oldState.isLoading
        )
        state = oldState.copy(viewState = viewState)
        viewStateLiveData.value = viewState
    }
}