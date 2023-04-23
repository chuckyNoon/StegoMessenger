package com.example.stegomessenger.features.stego_dialog

import android.content.ContentResolver
import com.example.stegomessenger.arch.redux.Action

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

    object SendingStarted : StegoAction()
    object SendingSuccess : StegoAction()
    object SendingFail : StegoAction()

    object Close : StegoAction()
}
