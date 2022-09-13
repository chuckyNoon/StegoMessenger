package com.example.diplomclient.search

import com.example.diplomclient.arch.redux.dispatcher.Dispatchable
import com.example.diplomclient.arch.redux.store.Middleware
import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.arch.network.ApiHelper
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
                    dispatchable.dispatch(CoreAction.ShowToast(it.message ?: "Some error occured"))
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
