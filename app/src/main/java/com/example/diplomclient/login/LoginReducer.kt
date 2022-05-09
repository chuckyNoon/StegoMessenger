package com.example.diplomclient.login

import com.aita.arch.store.Reducer
import com.example.diplomclient.arch.flux.Action

class LoginReducer : Reducer<LoginState> {

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
        val viewState = LoginViewState(
            isLoading = state.isLoading
        )
        return state.copy(
            viewState = viewState
        )
    }
}
