package com.example.stegomessenger.v2.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.stegomessenger.R
import com.example.stegomessenger.v2.compose.StegoTheme
import com.example.stegomessenger.v2.common.model.ChatViewState
import com.example.core.design.items.text_message.TextMessageCell
import com.example.stegomessenger.v2.features.chat.NewChatViewModel
import com.example.stegomessenger.v2.compose.views.TextMessage
import com.example.core.design.views.OutlineButtonStyle
import com.example.core.design.views.OutlinedStegoButton
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun ChatScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: NewChatViewModel = koinViewModel(),
    chatId: String = ""
) {
    val scaffoldState = rememberScaffoldState()
    val viewState by viewModel.viewStateLiveData.observeAsState(initial = ChatViewState.INITIAL)

    LaunchedEffect(key1 = chatId, block = {
        viewModel.init(chatId = chatId)
    })

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
                            text = viewState.chatName ?: "",
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
                                painter = painterResource(id = R.drawable.ic_24_back),
                                tint = Color.Black
                            )
                        }
                    }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                ) {

                    OutlinedStegoButton(
                        style = OutlineButtonStyle.MAIN,
                        text = "Text",
                        onClick = {

                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(width = 16.dp))

                    OutlinedStegoButton(
                        style = OutlineButtonStyle.MAIN,
                        text = "Image",
                        onClick = {

                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                LazyColumn {
                    items(viewState.cells) {
                        when (it) {
                            is TextMessageCell -> {
                                TextMessage(messageCell = it)
                            }
                        }
                    }
                }
            }
        }
    }
}

