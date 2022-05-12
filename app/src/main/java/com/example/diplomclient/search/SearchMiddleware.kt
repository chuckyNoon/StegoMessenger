package com.example.diplomclient.search

import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.main.navigation.CoreAction

class SearchMiddleware(private val apiHelper: ApiHelper) : Middleware<SearchState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: SearchState,
        newState: SearchState
    ) {
        when (action) {
            is SearchAction.ClickStartChat -> loadChatForUser(dispatchable, action.cell.idText)
            is SearchAction.TextTyped -> loadMatchingUsers(dispatchable, action.text)
        }
    }

    private fun loadChatForUser(
        dispatchable: Dispatchable,
        userId: String
    ) {
        launchBackgroundWork {
            safeApiCall(
                apiCall = {
                    apiHelper.sendText(
                        receiverId = userId,
                        text = "rr"
                    )
                },
                onSuccess = {
                    dispatchable.dispatch(CoreAction.ReloadChats)
                    dispatchable.dispatch(SearchAction.Back)
                },
                onError = {
                    dispatchable.dispatch(CoreAction.ShowError(it.message ?: "f2"))
                }
            )
        }
    }

    private fun loadMatchingUsers(
        dispatchable: Dispatchable,
        typedText: String
    ) {
        launchBackgroundWork {
            safeApiCall(
                apiCall = { apiHelper.search(typedText) },
                onSuccess = {
                    dispatchable.dispatch(
                        SearchAction.UsersLoaded(matchingUsers = it.matchingUsers)
                    )
                },
                onError = {
                    dispatchable.dispatch(CoreAction.ShowError(it.message ?: "f2"))
                }
            )
        }
    }
}