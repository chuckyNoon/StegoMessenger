package com.example.diplomclient.stego_dialog

import android.content.ContentResolver
import android.content.ContextWrapper
import com.example.diplomclient.arch.flux.Action

sealed class StegoAction : Action {
    data class Init(
        val receiverId: String
    ) : StegoAction()

    data class HandleImagePicked(val imageUriStr: String) : StegoAction()
    data class ClickSend(
        val contentResolver: ContentResolver
    ) : StegoAction()
}
