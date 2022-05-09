package com.example.diplomclient.main.navigation

import com.example.diplomclient.arch.flux.Action

sealed class CoreNavAction : Action {
    object ShowTestFragment : CoreNavAction()
    object ShowLogin : CoreNavAction()
    object ShowRegistration : CoreNavAction()
    data class ShowError(val text: String) : CoreNavAction()
}
