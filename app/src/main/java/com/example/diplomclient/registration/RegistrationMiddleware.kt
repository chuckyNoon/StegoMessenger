package com.example.diplomclient.registration

import android.util.Log
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.PrefsContract
import com.example.diplomclient.common.PrefsHelper
import com.example.diplomclient.main.navigation.CoreNavAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

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

    private fun handleLoginClick(dispatchable: Dispatchable, action: RegistrationAction.OnRegisterClick) {
        val login = action.login
        val password = action.password

        if (password.isNotEmpty() && login.isNotEmpty()) {
            dispatchable.dispatch(RegistrationAction.RegistrationStarted)
            GlobalScope.launch(Dispatchers.Main) {
                val token = register(login, password)
                if (!token.isNullOrEmpty()) {
                    PrefsHelper.getEditor().putString(PrefsContract.TOKEN, token).commit()
                    dispatchable.dispatch(RegistrationAction.RegistrationSuccess)
                    dispatchable.dispatch(CoreNavAction.ShowTestFragment)
                } else {
                    dispatchable.dispatch(RegistrationAction.RegistrationFail)
                }
            }
        } else {
            dispatchable.dispatch(RegistrationAction.InvalidAttempt)
        }
    }

    private suspend fun register(name: String, password: String): String? =
        try {
            val token = apiHelper.doRegister(name, password).value
            Log.d("network", token)
            token
        } catch (e: Exception) {
            Log.d("network", e.message.toString())
            null
        }
}
