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
    val isProgressBarVisible: Boolean
) {
    companion object {
        val EMPTY =
            LoginViewState(
                isProgressBarVisible = false
            )
    }
}
