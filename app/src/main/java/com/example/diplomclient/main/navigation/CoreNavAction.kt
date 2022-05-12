package com.example.diplomclient.main.navigation

import com.example.diplomclient.arch.flux.Action

sealed class CoreNavAction : Action {
    object ShowOverviewFragment : CoreNavAction()
    object ShowLogin : CoreNavAction()
    object ShowRegistration : CoreNavAction()
    object ShowChat : CoreNavAction()
    object ShowSearch : CoreNavAction()
    data class ShowError(val text: String) : CoreNavAction()
}
