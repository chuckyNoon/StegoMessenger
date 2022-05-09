package com.example.diplomclient.main.navigation

sealed class CoreNav {
    object Test : CoreNav()
    object Login : CoreNav()
    object Registration : CoreNav()
}
