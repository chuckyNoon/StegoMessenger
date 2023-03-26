package com.example.stegomessenger.v2.compose.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stegomessenger.v2.common.infra.DateTimeFormatter
import com.example.stegomessenger.v2.common.infra.StringsProvider
import com.example.stegomessenger.v2.compose.feature.sign_up.SignUpIntent
import com.example.stegomessenger.v2.compose.model.RegistrationState
import com.example.stegomessenger.v2.compose.model.RegistrationViewState
import com.example.stegomessenger.v2.compose.repository.ChatsRepository
import com.example.stegomessenger.v2.compose.repository.UserRepository
import com.example.stegomessenger.v2.new_arch.IntentHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewSignUpViewModel @Inject constructor(
    val chatsRepository: ChatsRepository,
    val dateTimeFormatter: DateTimeFormatter,
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