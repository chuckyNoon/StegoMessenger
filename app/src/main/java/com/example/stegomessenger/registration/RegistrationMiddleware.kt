package com.example.stegomessenger.registration

import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.common.network.ApiHelper
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.PrefsHelper
import com.example.stegomessenger.common.launchBackgroundWork
import com.example.stegomessenger.common.safeApiCall
import com.example.stegomessenger.main.navigation.CoreAction

class RegistrationMiddleware(
    private val apiHelper: ApiHelper
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
                        apiHelper.doRegister(
                            password = password,
                            id = id,
                            name = name
                        )
                    },
                    onSuccess = {
                        PrefsHelper.getEditor()
                            .putString(PrefsContract.TOKEN, it.value)
                            .commit()
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
