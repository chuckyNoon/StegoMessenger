package com.example.diplomclient.login

import com.example.diplomclient.arch.flux.Action

sealed class LoginAction() : Action {
    data class OnLoginClick(
        val name: String,
        val password: String
    ) : LoginAction()

    object LoginStarted : LoginAction()
    object LoginSuccess : LoginAction()
    object LoginFail : LoginAction()
    object InvalidAttempt : LoginAction()
}
