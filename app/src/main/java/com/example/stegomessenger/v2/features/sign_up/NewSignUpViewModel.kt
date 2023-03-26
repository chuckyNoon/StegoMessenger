package com.example.stegomessenger.v2.features.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stegomessenger.v2.common.infra.DateTimeFormatter
import com.example.stegomessenger.v2.common.infra.StringsProvider
import com.example.stegomessenger.v2.features.sign_up.SignUpIntent
import com.example.stegomessenger.v2.common.model.RegistrationState
import com.example.stegomessenger.v2.common.model.RegistrationViewState
import com.example.stegomessenger.v2.data.chat.ChatsRepository
import com.example.stegomessenger.v2.data.user.UserRepository
import com.example.stegomessenger.v2.arch.IntentHandler
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