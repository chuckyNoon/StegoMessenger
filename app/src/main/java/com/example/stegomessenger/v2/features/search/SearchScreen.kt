package com.example.stegomessenger.v2.screens

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.stegomessenger.R
import com.example.core.design.system.StegoTheme
import com.example.stegomessenger.v2.features.search.SearchIntent
import com.example.core.design.items.user.SearchUserCell
import com.example.stegomessenger.v2.common.model.SearchViewState
import com.example.stegomessenger.v2.features.search.NewSearchViewModel
import com.example.core.design.items.user.SearchUserItem
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: NewSearchViewModel = koinViewModel()
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
                            SearchUserItem(cell = it)
                        }
                    }
                }
            }
        }
    }
}