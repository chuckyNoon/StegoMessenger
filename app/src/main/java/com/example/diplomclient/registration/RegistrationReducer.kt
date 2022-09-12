package com.example.diplomclient.registration

import com.example.diplomclient.arch.redux.store.Reducer
import com.example.diplomclient.arch.redux.Action

class RegistrationReducer : Reducer<RegistrationState> {

    override fun acceptsAction(action: Action): Boolean = action is RegistrationAction

    override fun reduce(oldState: RegistrationState, action: Action): RegistrationState =
        when (action) {
            is RegistrationAction.RegistrationSuccess,
            is RegistrationAction.RegistrationFail ->
                rebuildViewState(oldState.copy(isLoading = false))
            is RegistrationAction.RegistrationStarted ->
                rebuildViewState(oldState.copy(isLoading = true))
            else -> oldState
        }

    private fun rebuildViewState(state: RegistrationState): RegistrationState {
        val viewState = RegistrationViewState(
            isLoading = state.isLoading
        )
        return state.copy(
            viewState = viewState
        )
    }
}
