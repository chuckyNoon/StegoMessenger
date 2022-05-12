package com.example.diplomclient.registration

import com.example.diplomclient.arch.flux.Action

sealed class RegistrationAction() : Action {
    data class OnRegisterClick(
        val login: String,
        val password: String,
        val id: String,
        val name: String
    ) : RegistrationAction()

    object RegistrationStarted : RegistrationAction()
    object RegistrationSuccess : RegistrationAction()
    object RegistrationFail : RegistrationAction()
    object InvalidAttempt : RegistrationAction()
}
