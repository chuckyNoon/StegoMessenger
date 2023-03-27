package com.example.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.util.PrefsContract
import com.example.core.infra.Prefs
import com.example.core.infra.StringsProvider
import com.example.core.network.ApiService
import com.example.core.util.safeExecute
import com.example.core.arch.IntentHandler
import com.example.core.R
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                    _viewActions.tryEmit(
                        LoginEvent.ShowSnackBar(
                        stringsProvider.getString(R.string.error)
                    ))
                }
            )

        }

}