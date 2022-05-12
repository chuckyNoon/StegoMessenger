package com.example.diplomclient.chat

import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.main.navigation.CoreAction
import com.example.diplomclient.search.SearchAction

class ChatMiddleware(
    private val apiHelper: ApiHelper
) : Middleware<ChatState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: ChatState,
        newState: ChatState
    ) {
        when (action) {
            is ChatAction.ClickSend -> sendTextMessage(newState, dispatchable)
        }
    }

    private fun sendTextMessage(newState: ChatState, dispatchable: Dispatchable) {
        val typedText = newState.typedText ?: return

        val chat = requireNotNull(newState.chat)
        val userId = chat.id

        launchBackgroundWork {
            safeApiCall(
                apiCall = {
                    apiHelper.sendText(
                        receiverId = userId,
                        text = typedText
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
}
