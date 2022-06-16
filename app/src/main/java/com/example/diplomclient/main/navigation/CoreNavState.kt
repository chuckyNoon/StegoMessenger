package com.example.diplomclient.main.navigation

import com.example.diplomclient.arch.flux.util.Event

data class CoreNavState(
    val navigationEvent: Event<CoreNav>?,
    val errorEvent: Event<String>?
) {
    companion object {
        val EMPTY = CoreNavState(navigationEvent = null, errorEvent = null)
    }
}
