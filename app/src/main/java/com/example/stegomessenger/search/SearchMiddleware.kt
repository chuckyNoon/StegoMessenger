package com.example.stegomessenger.search

import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.network.ApiHelper
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.common.launchBackgroundWork
import com.example.stegomessenger.common.safeApiCall
import com.example.stegomessenger.main.navigation.CoreAction

class SearchMiddleware(
    private val apiHelper: ApiHelper,
    private val stringsProvider: StringsProvider
) : Middleware<SearchState> {

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
        launchBackgroundWork {
            safeApiCall(
                apiCall = {
                    apiHelper.sendText(receiverId = userId, text = "") // serverside workaround
                },
                onSuccess = {
                    dispatchable.dispatch(CoreAction.ReloadChats)
                    dispatchable.dispatch(SearchAction.Back)
                },
                onError = {
                    dispatchable.dispatch(
                        CoreAction.ShowToast(
                            it.message ?: stringsProvider.getString(R.string.error)
                        )
                    )
                }
            )
        }

    private fun loadMatchingUsers(
        dispatchable: Dispatchable,
        typedText: String
    ) =
        launchBackgroundWork {
            safeApiCall(
                apiCall = { apiHelper.search(typedText) },
                onSuccess = {
                    dispatchable.dispatch(
                        SearchAction.UsersLoaded(matchingUsers = it.matchingUsers)
                    )
                },
                onError = {}
            )
        }
}
