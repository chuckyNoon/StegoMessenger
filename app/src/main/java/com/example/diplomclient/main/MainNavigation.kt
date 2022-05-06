package com.example.diplomclient.main

sealed class MainNavigation {
    object Test : MainNavigation()
    object Login : MainNavigation()
}
