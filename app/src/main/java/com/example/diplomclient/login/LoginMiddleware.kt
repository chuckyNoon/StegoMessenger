package com.example.diplomclient.login

import android.content.SharedPreferences
import com.example.diplomclient.arch.redux.dispatcher.Dispatchable
import com.example.diplomclient.arch.redux.store.Middleware
import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.PrefsContract
import com.example.diplomclient.common.PrefsHelper
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.main.navigation.CoreAction

class LoginMiddleware(
    private val apiHelper: ApiHelper,
    private val prefsEditor: SharedPreferences.Editor
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
                    apiCall = { apiHelper.doLogin(login, password) },
                    onSuccess = {
                        prefsEditor
                            .putString(PrefsContract.TOKEN, it.value)
                            .commit()
                        dispatchable.dispatch(LoginAction.LoginSuccess)
                        dispatchable.dispatch(CoreAction.ShowOverviewFragment)
                    },
                    onError = {
                        dispatchable.dispatch(CoreAction.ShowToast(it.message ?: "Some error occured"))
                        dispatchable.dispatch(LoginAction.LoginFail)
                    }
                )
            }
        } else {
            dispatchable.dispatch(LoginAction.InvalidAttempt)
        }
    }
}
