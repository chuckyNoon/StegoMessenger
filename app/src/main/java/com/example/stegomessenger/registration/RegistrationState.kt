package com.example.stegomessenger.registration

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
