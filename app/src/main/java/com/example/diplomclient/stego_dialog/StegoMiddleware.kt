package com.example.diplomclient.stego_dialog

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.getName
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.koch.Algorithm
import com.example.diplomclient.main.MainApplication
import com.example.diplomclient.main.navigation.CoreAction
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class StegoMiddleware(
    private val apiHelper: ApiHelper
) : Middleware<StegoState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: StegoState,
        newState: StegoState
    ) {
        when (action) {
            is StegoAction.ClickSend -> {
                when (newState.stateType) {
                    StegoStateType.IMAGE -> sendImage(dispatchable, newState, action)
                    StegoStateType.TEXT -> sendText(dispatchable, newState)
                }
            }
            else -> {
            }
        }
    }

    private fun sendText(dispatchable: Dispatchable, newState: StegoState) {
        val typedText = newState.contentText ?: return

        if (typedText.isEmpty()) {
            return
        }

        val userId = newState.receiverId ?: return

        dispatchable.dispatch(StegoAction.TextSendingStarted)

        launchBackgroundWork {
            safeApiCall(
                apiCall = {
                    apiHelper.sendText(
                        receiverId = userId,
                        text = typedText
                    )
                },
                onSuccess = {
                    dispatchable.dispatch(StegoAction.TextSendingSuccess)
                    dispatchable.dispatch(CoreAction.ReloadChats)
                    dispatchable.dispatch(CoreAction.ShowToast("Message was successfully sent"))
                    dispatchable.dispatch(StegoAction.Close)
                },
                onError = {
                    dispatchable.dispatch(StegoAction.TextSendingFail)
                    dispatchable.dispatch(CoreAction.ShowToast(it.message ?: "f2"))
                }
            )
        }
    }

    private fun sendImage(
        dispatchable: Dispatchable,
        newState: StegoState,
        action: StegoAction.ClickSend
    ) {
        val context = MainApplication.getInstance()
        val userId = requireNotNull(newState.receiverId)

        val containerUri = newState.containerUriStr?.let { Uri.parse(it) }
        val containerBitmap = containerUri?.let {
            MediaStore.Images.Media.getBitmap(action.contentResolver, it)
        }

        val contentUri = newState.contentUriStr?.let { Uri.parse(it) }
        val contentBitmap = contentUri?.let {
            MediaStore.Images.Media.getBitmap(action.contentResolver, it)
        }

        launchBackgroundWork {
            dispatchable.dispatch(StegoAction.ImageSendingStarted)

            val bitmapToSend = when {
                containerBitmap != null && contentBitmap != null -> {
                    Algorithm().lsbEncode(contentBitmap, containerBitmap) ?: error("failed to gen")
                }
                contentBitmap != null -> {
                    contentBitmap
                }
                else -> {
                    error("No valid bitmap")
                }
            }
            val file = File(
                context.getCacheDir(),
                contentUri.getName(context) ?: "wtf"
            )
            val os = BufferedOutputStream(FileOutputStream(file))
            bitmapToSend.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.close()

            safeApiCall(
                apiCall = {
                    apiHelper.sendImage(
                        receiverId = userId,
                        imageFile = file
                    )
                },
                onSuccess = {
                    AppLogger.log("file send s")
                    dispatchable.dispatch(StegoAction.ImageSendingSuccess)
                    dispatchable.dispatch(CoreAction.ReloadChats)
                    dispatchable.dispatch(CoreAction.ShowToast("Image was successfully sent"))
                },
                onError = {
                    AppLogger.log("file send f")
                    dispatchable.dispatch(StegoAction.ImageSendingFail)
                    dispatchable.dispatch(CoreAction.ShowToast(it.message ?: "no m"))
                }
            )
        }
    }
}
