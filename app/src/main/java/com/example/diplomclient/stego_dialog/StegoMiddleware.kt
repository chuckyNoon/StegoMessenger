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
        val imageUriStr = newState.imageUriStr
        val uri = Uri.parse(imageUriStr)
        val bitmap = MediaStore.Images.Media.getBitmap(action.contentResolver, uri)

        val userId = requireNotNull(newState.receiverId)

        launchBackgroundWork {
            val sourceBase64 = BitmapUtils.bitmapToBase64(bitmap)!!
            AppLogger.log("rrr ${sourceBase64.length}")

            safeApiCall(
                apiCall = {
                    apiHelper.sendImage(
                        receiverId = userId,
                        imageStr = sourceBase64
                    )
                },
                onSuccess = { response: SendImageResponse ->
                    val str = response.base64Str
                    val returnedBitmap = BitmapUtils.base64ToBitmap(str)
                    if (returnedBitmap != null) {
                        AppLogger.log("${returnedBitmap.height}-${returnedBitmap.width}")
                    } else {
                        AppLogger.log("zz")
                        for (i in str.indices) {
                            if (str[i] != sourceBase64[i]) {
                                AppLogger.log("${sourceBase64[i]}|${str[i]}|$i")
                            }
                        }
                    }
                    val test = BitmapUtils.base64ToBitmap(sourceBase64)
                    if (test == null) {
                        AppLogger.log("test")
                    }
                    AppLogger.log("file send s ${str.length}")
                },
                onError = {
                    AppLogger.log("file send f")
                    dispatchable.dispatch(CoreAction.ShowError(it.message ?: "no m"))
                }
            )
        }
    }
}
