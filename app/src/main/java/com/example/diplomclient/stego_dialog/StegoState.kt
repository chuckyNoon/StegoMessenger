package com.example.diplomclient.stego_dialog

import com.aita.arch.util.Event

data class StegoState(
    val viewState: StegoViewState,
    val receiverId: String?,
    val imageUriStr: String?,
    val containerUriStr: String?,
) {
    companion object {
        val EMPTY = StegoState(
            viewState = StegoViewState.EMPTY,
            receiverId = null,
            imageUriStr = null,
            containerUriStr = null,
        )
    }
}

data class StegoViewState(
    val isSendButtonAvailable: Boolean,
    val imageButtonText: String?,
) {
    companion object {
        val EMPTY = StegoViewState(
            isSendButtonAvailable = false,
            imageButtonText = null
        )
    }
}
