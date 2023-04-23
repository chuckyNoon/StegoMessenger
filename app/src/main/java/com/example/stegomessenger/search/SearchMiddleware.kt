package com.example.stegomessenger.search

import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.common.network.ApiService
import com.example.stegomessenger.main.navigation.CoreAction
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchMiddleware @Inject constructor(
    private val apiService: ApiService,
    private val stringsProvider: StringsProvider
) : Middleware<SearchState>() {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: SearchState,
        newState: SearchState
    ) {
        when (action) {
            is SearchAction.ClickStartChat -> loadChatForUser(dispatchable, userId = action.cell.id)
            is SearchAction.TextTyped -> loadMatchingUsers(dispatchable, action.text)
        }
    }

    private fun loadChatForUser(
        dispatchable: Dispatchable,
        userId: String
    ) =
        middlewareScope.launch {
            runCatching {
                // server-side workaround
                apiService.sendText(receiverId = userId, text = "")
            }.onSuccess {
                dispatchable.dispatch(CoreAction.ReloadChats)
                dispatchable.dispatch(SearchAction.Back)
            }.onFailure {
                dispatchable.dispatch(
                    CoreAction.ShowToast(
                        it.message ?: stringsProvider.getString(R.string.error)
                    )
                )
            }
        }

    private fun loadMatchingUsers(
        dispatchable: Dispatchable,
        typedText: String
    ) =
        middlewareScope.launch {
            runCatching { apiService.search(typedText) }
                .onSuccess {
                    dispatchable.dispatch(
                        SearchAction.UsersLoaded(matchingUsers = it.matchingUsers)
                    )
                }
        }
}
