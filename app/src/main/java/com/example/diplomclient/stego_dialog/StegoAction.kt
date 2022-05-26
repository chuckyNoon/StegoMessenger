package com.example.diplomclient.stego_dialog

import android.content.ContentResolver
import com.example.diplomclient.arch.flux.Action

sealed class StegoAction : Action {
    data class Init(
        val stateType: StegoStateType,
        val receiverId: String
    ) : StegoAction()

    object ClickCheckBox : StegoAction()
    data class HandleContainerPicked(val containerUriStr: String) : StegoAction()
    data class ClickSend(val contentResolver: ContentResolver) : StegoAction()

    data class HandleContentTextChanged(val text: String) : StegoAction()
    data class HandleContentImagePicked(val imageUriStr: String) : StegoAction()

    object ImageSendingStarted : StegoAction()
    object ImageSendingSuccess : StegoAction()
    object ImageSendingFail : StegoAction()

    object TextSendingStarted : StegoAction()
    object TextSendingSuccess : StegoAction()
    object TextSendingFail : StegoAction()
}
