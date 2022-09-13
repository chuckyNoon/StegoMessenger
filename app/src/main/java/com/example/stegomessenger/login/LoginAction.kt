package com.example.stegomessenger.login

import com.example.stegomessenger.arch.redux.Action

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
