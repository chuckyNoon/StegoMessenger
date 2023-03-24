package com.example.stegomessenger.compose.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stegomessenger.compose.nav.Screens

@Preview
@Composable
fun StegoApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.LOGIN.name) {
        composable(Screens.SIGN_UP.name) { SignUpScreen(navHostController = navController) }
        composable(Screens.LOGIN.name) { LoginScreen(navHostController = navController) }
        composable(Screens.OVERVIEW.name) { OverviewScreen(navHostController = navController) }
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
        composable(Screens.SEARCH.name) { SearchScreen(navHostController = navController) }
        /*...*/
    }
}