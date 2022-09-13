package com.example.diplomclient.login

import com.example.diplomclient.arch.redux.Action

sealed class LoginAction() : Action {
    data class ClickLogin(
        val login: String,
        val password: String
    ) : LoginAction()

    object LoginStarted : LoginAction()
    object LoginSuccess : LoginAction()
    object LoginFail : LoginAction()
    object InvalidAttempt : LoginAction()
}
