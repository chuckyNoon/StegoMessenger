package com.example.stegomessenger.registration

import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.launchBackgroundWork
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.common.safeApiCall
import com.example.stegomessenger.main.navigation.CoreAction
import javax.inject.Inject

class RegistrationMiddleware @Inject constructor(
    private val apiService: ApiService,
    private val prefs: Prefs
) : Middleware<RegistrationState> {

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
            launchBackgroundWork {
                safeApiCall(
                    apiCall = {
                        apiService.doRegister(
                            password = password,
                            id = id,
                            name = name
                        )
                    },
                    onSuccess = {
                        prefs.saveString(PrefsContract.TOKEN, it.value)
                        dispatchable.dispatch(RegistrationAction.RegistrationSuccess)
                        dispatchable.dispatch(CoreAction.ShowOverviewFragment)
                    },
                    onError = {
                        dispatchable.dispatch(CoreAction.ShowToast(it.message ?: "f2"))
                        dispatchable.dispatch(RegistrationAction.RegistrationFail)
                    }
                )
            }
        } else {
            dispatchable.dispatch(RegistrationAction.InvalidAttempt)
        }
    }
}
