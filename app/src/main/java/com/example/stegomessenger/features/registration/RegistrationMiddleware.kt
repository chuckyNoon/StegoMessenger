package com.example.stegomessenger.features.registration

import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.data.user.UserRepository
import com.example.stegomessenger.app.core.CoreAction
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationMiddleware @Inject constructor(
    private val userRepository: UserRepository,
) : Middleware<RegistrationState>() {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: RegistrationState,
        newState: RegistrationState
    ) {
        when (action) {
            is RegistrationAction.OnRegisterClick -> handleLoginClick(dispatchable, action)
        }
    }

    private fun handleLoginClick(
        dispatchable: Dispatchable,
        action: RegistrationAction.OnRegisterClick
    ) {
        val password = action.password
        val id = action.id
        val name = action.name

        if (password.isNotEmpty() && id.isNotEmpty() && name.isNotEmpty()) {
            dispatchable.dispatch(RegistrationAction.RegistrationStarted)
            middlewareScope.launch {
                runCatching { userRepository.register(password = password, id = id, name = name) }
                    .onSuccess {
                        dispatchable.dispatch(RegistrationAction.RegistrationSuccess)
                        dispatchable.dispatch(CoreAction.ShowOverviewFragment)
                    }
                    .onFailure {
                        dispatchable.dispatch(CoreAction.ShowToast(it.message ?: "f2"))
                        dispatchable.dispatch(RegistrationAction.RegistrationFail)
                    }
            }
        } else {
            dispatchable.dispatch(RegistrationAction.InvalidAttempt)
        }
    }
}
