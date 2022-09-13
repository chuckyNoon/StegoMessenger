package com.example.stegomessenger.login

import android.content.SharedPreferences
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.network.ApiHelper
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.launchBackgroundWork
import com.example.stegomessenger.common.safeApiCall
import com.example.stegomessenger.main.navigation.CoreAction

class LoginMiddleware(
    private val apiHelper: ApiHelper,
    private val prefsEditor: SharedPreferences.Editor,
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
                    apiCall = { apiHelper.doLogin(login, password) },
                    onSuccess = {
                        prefsEditor
                            .putString(PrefsContract.TOKEN, it.value)
                            .commit()
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
