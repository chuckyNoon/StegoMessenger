package com.example.features.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.core.design.system.StegoTheme
import com.example.core.design.views.OutlineButtonStyle
import com.example.core.design.views.OutlinedStegoButton
import com.example.features.base.Screens
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun LoginScreen(
    viewModel: NewLoginViewModel = koinViewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    val state by viewModel.state.observeAsState(LoginState.INITIAL)
    val viewAction = viewModel.viewActions().collectAsState(null).value
    val scaffoldState = rememberScaffoldState()

    StegoTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isInProgress) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier
                            .size(49.dp)
                            .align(Alignment.Center)
                            .progressSemantics()
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                ) {
                    OutlinedStegoButton(
                        style = OutlineButtonStyle.MAIN,
                        text = "Log in",
                        onClick = {
                            navHostController.navigate(Screens.OVERVIEW.name)
                        },
                        enabled = !state.isInProgress,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(height = 4.dp))

                    OutlinedStegoButton(
                        style = OutlineButtonStyle.SECONDARY,
                        text = "SIGN UP",
                        onClick = {
                            navHostController.navigate(Screens.SIGN_UP.name)
                        },
                        enabled = !state.isInProgress,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val x = ""
                    x.isDigitsOnly()
                }
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(56.dp))

                Text(
                    text = "Let's get started",
                    style = StegoTheme.typography.heading,
                    modifier = Modifier.padding(top = 24.dp)
                )
                TextField(
                    value = state?.userName ?: "",
                    onValueChange = {
                        viewModel.obtainIntent(LoginIntent.UserNameChanged(it))
                    },
                    label = { Text("User name") },
                    enabled = !state.isInProgress,
                    modifier = Modifier.padding(top = 32.dp).fillMaxWidth()
                )

                TextField(
                    value = state?.password ?: "",
                    onValueChange = {
                        viewModel.obtainIntent(LoginIntent.PasswordChanged(it))
                    },
                    label = { Text("Password") },
                    enabled = !state.isInProgress,
                    modifier = Modifier.padding(top = 24.dp).fillMaxWidth()
                )
            }
        }

    }

//    when (viewAction) {
//        is LoginEvent.ShowSnackBar -> {
//            LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
//                scaffoldState.snackbarHostState.showSnackbar(viewAction.text)
//            }
//        }
//        is LoginEvent.OpenOverview -> {
//            navHostController.navigate(Screens.OVERVIEW.name)
//        }
//        is LoginEvent.OpenSignUp -> {
//            Log.d("test", "heeee")
//            LaunchedEffect(viewAction) {
//                navHostController.navigate(Screens.SIGN_UP.name)
//            }
//        }
//        else -> {
//
//        }
//    }
}