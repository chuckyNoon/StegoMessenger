package com.example.stegomessenger.v2.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.example.stegomessenger.R
import com.example.core.design.system.StegoTheme
import com.example.core.design.items.chat.ChatCell
import com.example.stegomessenger.v2.common.model.OverviewViewState
import com.example.stegomessenger.v2.app.Screens
import com.example.stegomessenger.v2.features.overview.NewOverviewViewModel
import com.example.core.design.items.chat.ChatItem
import com.example.core.design.views.OutlineButtonStyle
import com.example.core.design.views.OutlinedStegoButton
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun OverviewScreen(
    viewModel: NewOverviewViewModel = koinViewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    val scaffoldState = rememberScaffoldState()
    val viewState by viewModel.viewStateLiveData.observeAsState(initial = OverviewViewState.INITIAL)

    StegoTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 4.dp,
                    title = {
                        Text(
                            text = "Chats",
                            color = Color.Black,
                            style = StegoTheme.typography.body
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navHostController.popBackStack()
                        }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                contentDescription = "",
                                painter = painterResource(id = R.drawable.ic_24_close),
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navHostController.navigate(
                                    route = Screens.SEARCH.name,
                                    navOptions = NavOptions.Builder()
                                        .setEnterAnim(R.anim.slide_in_from_bottom)
                                        .setExitAnim(R.anim.slide_out_to_bottom)
                                        .build()
                                )
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                contentDescription = "",
                                painter = painterResource(id = R.drawable.ic_24_search),
                                tint = Color.Black
                            )
                        }
                    }
                )
            },
            drawerContent = {
                OutlinedStegoButton(
                    style = OutlineButtonStyle.MAIN,
                    text = "123",
                    onClick = { }
                )
            }
        ) {
            LazyColumn {
                items(viewState.cells) {
                    when (it) {
                        is ChatCell -> ChatItem(
                            cell = it,
                            onClick = { cell: ChatCell ->
                                navHostController.navigate("chat?chatId=${cell.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}