package com.example.diplomclient.main.navigation

sealed class CoreNav {
    object Overview : CoreNav()
    object Login : CoreNav()
    object Registration : CoreNav()
    object Chat : CoreNav()
}
