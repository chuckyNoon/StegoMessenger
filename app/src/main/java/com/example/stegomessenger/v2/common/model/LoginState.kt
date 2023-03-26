package com.example.stegomessenger.v2.common.model


data class LoginState(
    val userName: String?,
    val password: String?,
    val isInProgress: Boolean,
    val isLoginBtnEnabled: Boolean,
    val isTextFieldsEnabled: Boolean
) {
    companion object {
        val INITIAL = LoginState(
            userName = null,
            password = null,
            isInProgress = false,
            isLoginBtnEnabled = false,
            isTextFieldsEnabled = true
        )
    }
}

