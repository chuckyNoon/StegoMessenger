package com.example.features.login

sealed class LoginEvent {
    data class ShowSnackBar(val text: String) : LoginEvent()
    object OpenOverview: LoginEvent()
    object OpenSignUp: LoginEvent()
}