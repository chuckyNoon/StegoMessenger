package com.example.diplomclient.chat

import android.graphics.Bitmap
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.BitmapUtils
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
            is ChatAction.FilePicked -> handleFilePicked(action.bitmap, dispatchable)
        }
    }

    private fun handleFilePicked(bitmap: Bitmap, dispatchable: Dispatchable) {
        AppLogger.log("ffr ${bitmap.width}")

        launchBackgroundWork {
            val base64Str = BitmapUtils.bitmapToBase64(bitmap)!!
            AppLogger.log(base64Str.length.toString())

            safeApiCall(
                apiCall = {
                    apiHelper.sendImage(imageStr = base64Str)
                },
                onSuccess = {
                    AppLogger.log("file send s")
                },
                onError = {
                    AppLogger.log("file send f")
                    dispatchable.dispatch(CoreAction.ShowError(it.message ?: "no m"))
                }
            )
        }
    }

    private fun sendTextMessage(newState: ChatState, dispatchable: Dispatchable) {
        val typedText = newState.typedText ?: return

        if (typedText.isEmpty()) {
            return
        } else {
            dispatchable.dispatch(ChatAction.CompleteSending)
        }

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
