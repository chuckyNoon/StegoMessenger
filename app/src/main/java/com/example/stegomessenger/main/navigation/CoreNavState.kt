package com.example.stegomessenger.main.navigation

import com.example.stegomessenger.arch.redux.util.Event

data class CoreNavState(
    val navigationEvent: Event<CoreNav>?,
    val errorEvent: Event<String>?
) {
    companion object {
        val EMPTY = CoreNavState(navigationEvent = null, errorEvent = null)
    }
}
