package com.example.diplomclient.stego_dialog

import android.content.ContentResolver
import android.graphics.Bitmap
import com.example.diplomclient.arch.flux.Action

sealed class StegoAction : Action {
    data class Init(
        val receiverId: String
    ) : StegoAction()

    data class HandleImagePicked(val imageUriStr: String) : StegoAction()
    data class HandleContainerPicked(val containerUriStr: String) : StegoAction()
    data class ClickSend(
        val contentResolver: ContentResolver
    ) : StegoAction()

    data class UpdateDisplayImage(
        val bitmap: Bitmap
    ) : StegoAction()

    object ImageSendingStarted : StegoAction()
    object ImageSendingSuccess : StegoAction()
    object ImageSendingFail : StegoAction()
}
