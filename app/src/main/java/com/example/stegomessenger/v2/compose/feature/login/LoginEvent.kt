package com.example.stegomessenger.v2.compose.feature.login

sealed class LoginEvent {
    data class ShowSnackBar(val text: String) : LoginEvent()
    object OpenOverview: LoginEvent()
    object OpenSignUp: LoginEvent()
}