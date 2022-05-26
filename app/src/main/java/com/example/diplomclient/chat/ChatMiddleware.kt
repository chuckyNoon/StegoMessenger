package com.example.diplomclient.chat

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.main.MainApplication
import com.example.diplomclient.stego_dialog.StegoAction
import com.example.diplomclient.stego_dialog.StegoStateType
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class ChatMiddleware : Middleware<ChatState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: ChatState,
        newState: ChatState
    ) {
        when (action) {
            is ChatAction.ClickSendImage -> {
                val receiverId = newState.chat!!.id
                dispatchable.dispatch(
                    StegoAction.Init(
                        stateType = StegoStateType.IMAGE,
                        receiverId = receiverId
                    )
                )
            }
            is ChatAction.ClickSendText -> {
                val receiverId = newState.chat!!.id
                dispatchable.dispatch(
                    StegoAction.Init(
                        stateType = StegoStateType.TEXT,
                        receiverId = receiverId
                    )
                )
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
}
