package com.example.stegomessenger.main.navigation

sealed class CoreNav {
    object Overview : CoreNav()
    object Login : CoreNav()
    object Registration : CoreNav()
    object Chat : CoreNav()
    object Search : CoreNav()
    object Result : CoreNav()
    object StegoDialog : CoreNav()
    object ContentDialog : CoreNav()
}
