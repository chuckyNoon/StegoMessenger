package com.example.features.login

sealed class LoginIntent {
    data class UserNameChanged(val text: String) : LoginIntent()
    data class PasswordChanged(val text: String) : LoginIntent()
    object LoginClicked : LoginIntent()
    object SignUpClicked : LoginIntent()
}