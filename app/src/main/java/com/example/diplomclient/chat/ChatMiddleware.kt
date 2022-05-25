package com.example.diplomclient.chat

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.main.MainApplication
import com.example.diplomclient.main.navigation.CoreAction
import com.example.diplomclient.search.SearchAction
import com.example.diplomclient.stego_dialog.StegoAction
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

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
            is ChatAction.ClickImage -> {
                val receiverId = newState.chat!!.id
                dispatchable.dispatch(StegoAction.Init(receiverId))
            }
            is ChatAction.ClickSendText -> {
                val receiverId = newState.chat!!.id
                dispatchable.dispatch(StegoAction.Init(receiverId))
            }
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(MainApplication.getInstance())
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)

        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 80, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
        return directory.absolutePath
    }

    private fun loadFileBites(path: String) {
        try {
            val f = File(path, "profile.jpg")

            AppLogger.log("file size = " + f.length().toString())
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
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
                    dispatchable.dispatch(CoreAction.ShowToast(it.message ?: "f2"))
                }
            )
        }
    }
}
