package com.example.diplomclient.main

import com.aita.arch.util.Event

data class MainState(
    val navigationEvent: Event<MainNavigation>?
) {
    companion object {
        val EMPTY = MainState(navigationEvent = null)
    }
}
