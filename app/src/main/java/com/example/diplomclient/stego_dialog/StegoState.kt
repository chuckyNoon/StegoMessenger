package com.example.diplomclient.stego_dialog

import android.graphics.Bitmap
import com.aita.arch.util.Event

data class StegoState(
    val viewState: StegoViewState,
    val receiverId: String?,
    val imageUriStr: String?,
    val containerUriStr: String?,
    val displayBitmap: Bitmap?,
    val isInPgoress: Boolean,
    val closeEvent: Event<Unit>?
) {
    companion object {
        val EMPTY = StegoState(
            viewState = StegoViewState.EMPTY,
            receiverId = null,
            imageUriStr = null,
            containerUriStr = null,
            displayBitmap = null,
            isInPgoress = false,
            closeEvent = null
        )
    }
}

data class StegoViewState(
    val isSendButtonAvailable: Boolean,
    val imageButtonText: String?,
    val containerButtonText: String?,
    val displayBitmap: Bitmap?,
    val isInPgoress: Boolean
) {
    companion object {
        val EMPTY = StegoViewState(
            isSendButtonAvailable = false,
            imageButtonText = null,
            containerButtonText = null,
            displayBitmap = null,
            isInPgoress = false
        )
    }
}
