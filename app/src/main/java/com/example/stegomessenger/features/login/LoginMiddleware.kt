package com.example.stegomessenger.features.login

import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.data.user.UserRepository
import com.example.stegomessenger.app.core.CoreAction
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginMiddleware @Inject constructor(
    private val userRepository: UserRepository,
    private val stringsProvider: StringsProvider
) : Middleware<LoginState>() {

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

        if (login.isEmpty() || password.isEmpty()) {
            dispatchable.dispatch(LoginAction.InvalidAttempt)
            return
        }

        dispatchable.dispatch(LoginAction.LoginStarted)
        middlewareScope.launch {
            runCatching { userRepository.logIn(login, password) }
                .onSuccess {
                    dispatchable.dispatch(LoginAction.LoginSuccess)
                    dispatchable.dispatch(CoreAction.ShowOverviewFragment)
                }
                .onFailure {
                    dispatchable.dispatch(
                        CoreAction.ShowToast(
                            it.message ?: stringsProvider.getString(R.string.error)
                        )
                    )
                    dispatchable.dispatch(LoginAction.LoginFail)
                }
        }
    }
}
