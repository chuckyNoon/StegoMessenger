package com.example.stegomessenger.registration

data class RegistrationState(
    val isLoading: Boolean,
    val viewState: RegistrationViewState
) {
    companion object {
        val EMPTY = RegistrationState(
            isLoading = false,
            viewState = RegistrationViewState.EMPTY
        )
    }
}

data class RegistrationViewState(
    val isLoading: Boolean
) {
    companion object {
        val EMPTY =
            RegistrationViewState(
                isLoading = false
            )
    }
}
