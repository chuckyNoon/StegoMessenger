package com.example.diplomclient.login

import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.PrefsContract
import com.example.diplomclient.common.PrefsHelper
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.main.navigation.CoreAction

class LoginMiddleware(
    private val apiHelper: ApiHelper
) : Middleware<LoginState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: LoginState,
        newState: LoginState
    ) {
        when (action) {
            is LoginAction.OnLoginClick -> handleLoginClick(dispatchable, action)
        }
    }

    private fun handleLoginClick(dispatchable: Dispatchable, action: LoginAction.OnLoginClick) {
        val login = action.login
        val password = action.password

        if (login.isNotEmpty() && password.isNotEmpty()) {
            dispatchable.dispatch(LoginAction.LoginStarted)
            launchBackgroundWork {
                safeApiCall(
                    apiCall = { apiHelper.doLogin(login, password) },
                    onSuccess = {
                        PrefsHelper.getEditor()
                            .putString(PrefsContract.TOKEN, it.value)
                            .commit()
                        dispatchable.dispatch(LoginAction.LoginSuccess)
                        dispatchable.dispatch(CoreAction.ShowOverviewFragment)
                    },
                    onError = {
                        dispatchable.dispatch(CoreAction.ShowError(it.message ?: "f2"))
                        dispatchable.dispatch(LoginAction.LoginFail)
                    }
                )
            }
        } else {
            dispatchable.dispatch(LoginAction.InvalidAttempt)
        }
    }
}
