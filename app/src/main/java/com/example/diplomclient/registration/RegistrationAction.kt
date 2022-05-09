package com.example.diplomclient.registration

import com.example.diplomclient.arch.flux.Action

sealed class RegistrationAction() : Action {
    data class OnRegisterClick(
        val visibleName: String,
        val login: String,
        val password: String
    ) : RegistrationAction()

    object RegistrationStarted : RegistrationAction()
    object RegistrationSuccess : RegistrationAction()
    object RegistrationFail : RegistrationAction()
    object InvalidAttempt : RegistrationAction()
}
