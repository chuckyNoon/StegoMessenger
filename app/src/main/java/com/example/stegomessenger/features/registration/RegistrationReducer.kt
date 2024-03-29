package com.example.stegomessenger.features.registration

import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.arch.redux.Action
import javax.inject.Inject

class RegistrationReducer @Inject constructor(): Reducer<RegistrationState> {

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
