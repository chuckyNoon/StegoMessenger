package com.example.stegomessenger.compose.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.stegomessenger.R
import com.example.stegomessenger.compose.StegoTheme
import com.example.stegomessenger.compose.feature.search.SearchIntent
import com.example.stegomessenger.compose.viewmodels.NewSearchViewModel
import com.example.stegomessenger.compose.views.UserItem
import com.example.stegomessenger.search.SearchViewState
import com.example.stegomessenger.search.item.SearchUserCell

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: NewSearchViewModel = hiltViewModel()
) {

    val viewState by viewModel.viewStateLiveData.observeAsState(initial = SearchViewState.INITIAL)

    StegoTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 4.dp,
                    title = {
                        TextField(value = viewState.searchText ?: "", onValueChange = {
                            viewModel.obtainIntent(SearchIntent.TextTyped(text = it))
                        })
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
                    },
                )
            }
        ) {
            LazyColumn {
                items(viewState.cells) {
                    when (it) {
                        is SearchUserCell -> {
                            UserItem(cell = it)
                        }
                    }
                }
            }
        }
    }
}