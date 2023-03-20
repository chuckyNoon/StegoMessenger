package com.example.stegomessenger.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stegochat.ui.theme.StegoChatTheme
import kotlinx.coroutines.launch


class TestViewModel : ViewModel() {
    var state = MutableLiveData(
        LoginState(text = "3")
    )

    fun doSth(newText: String) {
        viewModelScope.launch{
            state.value = state.value?.copy(
                text = newText
            )
        }
    }
}

data class LoginState(
    val text: String
)

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    viewModel: TestViewModel = viewModel()
) {
    val state by viewModel.state.observeAsState()

    StegoChatTheme {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = "Let's get started",
                style = MaterialTheme.typography.h1,
            )
            TextField(
                value = state?.text ?: "",
                onValueChange = { it: String ->
                    viewModel.doSth(it)
                },
                label = { Text("lavel") }
            )
        }
    }
}