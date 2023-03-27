package com.example.features.sign_up

sealed class SignUpIntent {
    data class OnRegisterClick(
        val login: String,
        val password: String,
        val name: String
    ) : SignUpIntent()
}