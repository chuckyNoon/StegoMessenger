package com.example.stegomessenger.v2.common.model

data class RegistrationState(
    val isLoading: Boolean,
    val viewState: RegistrationViewState
) {
    companion object {
        val INITIAL = RegistrationState(
            isLoading = false,
            viewState = RegistrationViewState.INITIAL
        )
    }
}

data class RegistrationViewState(
    val isLoading: Boolean
) {
    companion object {
        val INITIAL =
            RegistrationViewState(
                isLoading = false
            )
    }
}
