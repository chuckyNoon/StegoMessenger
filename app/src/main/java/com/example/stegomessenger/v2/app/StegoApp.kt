package com.example.stegomessenger.v2.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.features.chat.ChatScreen
import com.example.features.login.LoginScreen
import com.example.features.overview.OverviewScreen
import com.example.features.search.SearchScreen
import com.example.features.sign_up.SignUpScreen

@Preview
@Composable
fun StegoApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = com.example.features.base.Screens.LOGIN.name) {
        composable(com.example.features.base.Screens.SIGN_UP.name) { SignUpScreen(navHostController = navController) }
        composable(com.example.features.base.Screens.LOGIN.name) { LoginScreen(navHostController = navController) }
        composable(com.example.features.base.Screens.OVERVIEW.name) { OverviewScreen(navHostController = navController) }
        composable(
            route = "chat?chatId={chatId}",
            arguments = listOf(
                navArgument("chatId") {
                    defaultValue = ""
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val chatId = navBackStackEntry.arguments?.getString("chatId")
            chatId?.let {
                ChatScreen(navHostController = navController, chatId = it)
            }
        }
        composable(com.example.features.base.Screens.SEARCH.name) { SearchScreen(navHostController = navController) }
        /*...*/
    }
}