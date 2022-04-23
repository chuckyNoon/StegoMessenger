package com.example.diplomclient.test

import com.example.diplomclient.arch.flux.Action

sealed class TestAction : Action {
    object Init : TestAction()
}
