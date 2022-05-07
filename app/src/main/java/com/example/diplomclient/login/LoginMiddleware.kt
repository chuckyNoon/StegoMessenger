package com.example.diplomclient.login

import android.util.Log
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.PrefsContract
import com.example.diplomclient.common.PrefsHelper
import com.example.diplomclient.main.MainAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

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
        val name = action.name
        val password = action.password

        if (name.isNotEmpty() && password.isNotEmpty()) {
            dispatchable.dispatch(LoginAction.LoginStarted)
            GlobalScope.launch(Dispatchers.Main) {
                val token = loadToken(name, password)
                if (!token.isNullOrEmpty()) {
                    PrefsHelper.getEditor().putString(PrefsContract.TOKEN, token).commit()
                    dispatchable.dispatch(LoginAction.LoginSuccess)
                    dispatchable.dispatch(MainAction.ShowTestFragment)
                } else {
                    dispatchable.dispatch(LoginAction.LoginFail)
                }
            }
        } else {
            dispatchable.dispatch(LoginAction.InvalidAttempt)
        }
    }

    private suspend fun loadToken(name: String, password: String): String? =
        try {
            val token = apiHelper.getToken(name, password).token
            Log.d("network", token)
            token
        } catch (e: Exception) {
            Log.d("network", e.message.toString())
            null
        }
}
