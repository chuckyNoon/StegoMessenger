package com.example.stegomessenger.compose.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.stegomessenger.R
import com.example.stegomessenger.chat.ChatViewModel
import com.example.stegomessenger.chat.ChatViewState
import com.example.stegomessenger.chat.items.TextMessageCell
import com.example.stegomessenger.compose.StegoTheme
import com.example.stegomessenger.compose.model.OverviewViewState
import com.example.stegomessenger.compose.viewmodels.NewChatViewModel
import com.example.stegomessenger.compose.views.OutlineButtonStyle
import com.example.stegomessenger.compose.views.OutlinedStegoButton
import com.example.stegomessenger.compose.views.TextMessage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun ChatScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: NewChatViewModel = hiltViewModel(),
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
                        IconButton(onClick = {}) {
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

