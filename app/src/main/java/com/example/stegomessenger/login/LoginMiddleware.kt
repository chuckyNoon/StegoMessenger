package com.example.stegomessenger.login

import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.launchBackgroundWork
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.common.safeApiCall
import com.example.stegomessenger.main.navigation.CoreAction

class LoginMiddleware(
    private val apiService: ApiService,
    private val prefs: Prefs,
    private val stringsProvider: StringsProvider
) : Middleware<LoginState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: LoginState,
        newState: LoginState
    ) {
        when (action) {
            is LoginAction.ClickLogin -> handleClickLogin(dispatchable, action)
        }
    }

    private fun handleClickLogin(dispatchable: Dispatchable, action: LoginAction.ClickLogin) {
        val login = action.login
        val password = action.password

        if (login.isNotEmpty() && password.isNotEmpty()) {
            dispatchable.dispatch(LoginAction.LoginStarted)
            launchBackgroundWork {
                safeApiCall(
                    apiCall = { apiService.doLogin(login, password) },
                    onSuccess = {
                        prefs.saveString(PrefsContract.TOKEN, it.value)
                        dispatchable.dispatch(LoginAction.LoginSuccess)
                        dispatchable.dispatch(CoreAction.ShowOverviewFragment)
                    },
                    onError = {
                        dispatchable.dispatch(
                            CoreAction.ShowToast(
                                it.message ?: stringsProvider.getString(R.string.error)
                            )
                        )
                        dispatchable.dispatch(LoginAction.LoginFail)
                    }
                )
            }
        } else {
            dispatchable.dispatch(LoginAction.InvalidAttempt)
        }
    }
}
