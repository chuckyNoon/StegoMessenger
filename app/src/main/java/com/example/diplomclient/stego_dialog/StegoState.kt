package com.example.diplomclient.stego_dialog

import com.aita.arch.util.Event

data class StegoState(
    val stateType: StegoStateType,
    val viewState: StegoViewState,
    val receiverId: String?,
    val contentText: String?,
    val contentUriStr: String?,
    val containerUriStr: String?,
    val isInPgoress: Boolean,
    val isStegoSelected: Boolean,
    val closeEvent: Event<Unit>?,
) {
    companion object {
        val EMPTY = StegoState(
            stateType = StegoStateType.TEXT,
            viewState = StegoViewState.EMPTY,
            receiverId = null,
            isInPgoress = false,
            isStegoSelected = false,
            contentUriStr = null,
            contentText = null,
            containerUriStr = null,
            closeEvent = null,
        )
    }
}

enum class StegoStateType {
    TEXT,
    IMAGE
}

sealed class StegoViewState(
    open val titleText: String?,
    open val isStegoCheckBoxSelected: Boolean,
    open val containerBitmapUriStr: String?,
    open val isSendButtonEnabled: Boolean,
    open val isProgressBarVisible: Boolean
) {
    data class Text(
        override val titleText: String?,
        override val isStegoCheckBoxSelected: Boolean,
        override val containerBitmapUriStr: String?,
        override val isSendButtonEnabled: Boolean,
        override val isProgressBarVisible: Boolean,
        val contentText: String?,
    ) : StegoViewState(
        titleText,
        isStegoCheckBoxSelected,
        containerBitmapUriStr,
        isSendButtonEnabled,
        isProgressBarVisible
    )

    data class Image(
        override val titleText: String?,
        override val isStegoCheckBoxSelected: Boolean,
        override val containerBitmapUriStr: String?,
        override val isSendButtonEnabled: Boolean,
        override val isProgressBarVisible: Boolean,
        val contentBitmapUriStr: String?
    ) : StegoViewState(
        titleText,
        isStegoCheckBoxSelected,
        containerBitmapUriStr,
        isSendButtonEnabled,
        isProgressBarVisible
    )

    companion object {
        val EMPTY = Text(
            titleText = null,
            isSendButtonEnabled = false,
            containerBitmapUriStr = null,
            isStegoCheckBoxSelected = false,
            isProgressBarVisible = false,
            contentText = null
        )
    }
}
