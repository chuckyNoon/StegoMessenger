package com.example.stegomessenger.compose.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.common.safeExecute
import com.example.stegomessenger.compose.LoginState
import com.example.stegomessenger.compose.feature.login.LoginEvent
import com.example.stegomessenger.compose.feature.login.LoginIntent
import com.example.stegomessenger.new_arch.IntentHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewLoginViewModel @Inject constructor(
    val apiService: ApiService,
    val prefs: Prefs,
    val stringsProvider: StringsProvider
) : ViewModel(), IntentHandler<LoginIntent> {

    var state = MutableLiveData(LoginState.INITIAL)

    private val _viewActions = MutableSharedFlow<LoginEvent>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun viewActions(): SharedFlow<LoginEvent> = _viewActions.asSharedFlow()

    override fun obtainIntent(intent: LoginIntent) {
        val oldState = state.value ?: return

        when (intent) {
            is LoginIntent.UserNameChanged -> {
                state.value = oldState.copy(userName = intent.text)
            }
            is LoginIntent.PasswordChanged -> {
                state.value= oldState.copy(password = intent.text)
            }
            is LoginIntent.LoginClicked -> {
                state.value = oldState.copy(
                    isInProgress = true
                )
                logIn()
            }
            is LoginIntent.SignUpClicked -> {
                _viewActions.tryEmit(LoginEvent.OpenSignUp)
            }
        }
    }

    private fun logIn() =
        viewModelScope.launch {
            val oldState = state.value ?: return@launch
            val userName = oldState.userName ?: return@launch
            val password = oldState.password ?: return@launch

            safeExecute(
                block = {
                    apiService.doLogin(
                        id = userName,
                        password = password
                    )
                },
                onSuccess = {
                    state.value = oldState.copy(isInProgress = false)
                    prefs.saveString(PrefsContract.TOKEN, it.value)
                    _viewActions.tryEmit(LoginEvent.OpenOverview)
                },
                onError = {
                    _viewActions.tryEmit(LoginEvent.ShowSnackBar(
                        stringsProvider.getString(R.string.error)
                    ))
                }
            )

        }

}