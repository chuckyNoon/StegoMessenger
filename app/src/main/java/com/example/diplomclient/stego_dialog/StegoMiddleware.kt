package com.example.diplomclient.stego_dialog

import android.net.Uri
import android.provider.MediaStore
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.arch.network.model.SendImageResponse
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.BitmapUtils
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.koch.Algorithm
import com.example.diplomclient.main.navigation.CoreAction

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
                handleClickSend(dispatchable, newState, action)
            }
            else -> {
            }
        }
    }

    private fun handleClickSend(
        dispatchable: Dispatchable,
        newState: StegoState,
        action: StegoAction.ClickSend
    ) {
        val userId = requireNotNull(newState.receiverId)

        val imageUriStr = newState.imageUriStr
        val uri = Uri.parse(imageUriStr)
        val bitmap = MediaStore.Images.Media.getBitmap(action.contentResolver, uri)

        val containerUriStr = newState.containerUriStr
        val containerUri = Uri.parse(containerUriStr)
        val containerBitmap =
            MediaStore.Images.Media.getBitmap(action.contentResolver, containerUri)

        launchBackgroundWork {
            dispatchable.dispatch(StegoAction.ImageSendingStarted)
            val stegoBitmap = Algorithm().lsbEncode(bitmap, containerBitmap)!!
            val base64Stego = BitmapUtils.bitmapToBase64(stegoBitmap, isLossless = true)!!
            safeApiCall(
                apiCall = {
                    apiHelper.sendImage(
                        receiverId = userId,
                        imageStr = base64Stego
                    )
                },
                onSuccess = { response: SendImageResponse ->
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
