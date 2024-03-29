package com.example.stegomessenger.features.login

import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.arch.redux.Action
import javax.inject.Inject

class LoginReducer @Inject constructor() : Reducer<LoginState> {

    override fun acceptsAction(action: Action): Boolean = action is LoginAction

    override fun reduce(oldState: LoginState, action: Action): LoginState =
        when (action) {
            is LoginAction.LoginSuccess,
            is LoginAction.LoginFail ->
                rebuildViewState(oldState.copy(isLoading = false))
            is LoginAction.LoginStarted ->
                rebuildViewState(oldState.copy(isLoading = true))
            else -> oldState
        }

    private fun rebuildViewState(state: LoginState): LoginState {
        val isLoading = state.isLoading
        val viewState = LoginViewState(
            isLoginButtonEnabled = !isLoading,
            isRegistrationButtonEnabled = !isLoading,
            isNameEditTextEnabled = !isLoading,
            isPasswordEditTextEnabled = !isLoading,
            isProgressBarVisible = isLoading
        )
        return state.copy(viewState = viewState)
    }
}
