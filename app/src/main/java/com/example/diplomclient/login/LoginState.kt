package com.example.diplomclient.login

data class LoginState(
    val isLoading: Boolean,
    val viewState: LoginViewState
) {
    companion object {
        val EMPTY = LoginState(
            isLoading = false,
            viewState = LoginViewState.EMPTY
        )
    }
}

data class LoginViewState(
    val isLoginButtonEnabled: Boolean,
    val isRegistrationButtonEnabled: Boolean,
    val isNameEditTextEnabled: Boolean,
    val isPasswordEditTextEnabled: Boolean,
    val isProgressBarVisible: Boolean
) {
    companion object {
        val EMPTY =
            LoginViewState(
                isLoginButtonEnabled = true,
                isRegistrationButtonEnabled = true,
                isNameEditTextEnabled = true,
                isPasswordEditTextEnabled = true,
                isProgressBarVisible = false
            )
    }
}
