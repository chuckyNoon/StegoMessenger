package com.example.diplomclient.main

import com.example.diplomclient.arch.flux.Action

sealed class MainAction : Action {
    object ShowTestFragment : MainAction()
    object ShowLogin : MainAction()
}
