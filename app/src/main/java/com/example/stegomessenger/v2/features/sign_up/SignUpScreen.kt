package com.example.stegomessenger.v2.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.stegomessenger.R
import com.example.core.design.system.StegoTheme
import com.example.stegomessenger.v2.features.sign_up.SignUpIntent
import com.example.stegomessenger.v2.common.model.RegistrationViewState
import com.example.stegomessenger.v2.features.sign_up.NewSignUpViewModel
import com.example.core.design.views.OutlineButtonStyle
import com.example.core.design.views.OutlinedStegoButton
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SignUpScreen(
    viewModel: NewSignUpViewModel = koinViewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    val scaffoldState = rememberScaffoldState()
    val viewState by viewModel.viewStateLiveData.observeAsState(initial = RegistrationViewState.INITIAL)

    StegoTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 4.dp,
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            navHostController.popBackStack()
                        }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                contentDescription = "",
                                painter = painterResource(id = R.drawable.ic_24_back),
                                tint = Color.Black
                            )
                        }
                    }
                )
            },
            scaffoldState = scaffoldState,
            modifier = Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (viewState.isLoading) {
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
                        text = "Sign Up",
                        onClick = {
                            viewModel.obtainIntent(
                                SignUpIntent.OnRegisterClick(
                                    login = "f",
                                    password = "f",
                                    name = "34"
                                )
                            )
                        },
                        enabled = !viewState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                Text(
                    text = "Registration",
                    style = StegoTheme.typography.heading,
                    modifier = Modifier.padding(top = 16.dp)
                )
                TextField(
                    value = "",
                    onValueChange = {
                        // viewModel.obtainIntent(LoginIntent.UserNameChanged(it))
                    },
                    label = { Text("User name") },
                    enabled = !viewState.isLoading,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth()
                )

                TextField(
                    value = "",
                    onValueChange = {

                    },
                    label = { Text("Password") },
                    enabled = !viewState.isLoading,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                )

                TextField(
                    value = "",
                    onValueChange = {

                    },
                    label = { Text("ID") },
                    enabled = !viewState.isLoading,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                )
            }
        }

    }
}