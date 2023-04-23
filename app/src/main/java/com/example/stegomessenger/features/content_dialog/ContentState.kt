package com.example.stegomessenger.features.content_dialog

import android.graphics.Bitmap
import com.example.stegomessenger.arch.adapter.DelegateDiffable

data class ContentState(
    val type: ContentStateType,
    val text: String?,
    val image: Bitmap?,
    val viewState: ContentViewState
) {
    companion object {
        val EMPTY = ContentState(
            type = ContentStateType.TEXT,
            viewState = ContentViewState.EMPTY,
            text = null,
            image = null,
        )
    }
}

enum class ContentStateType {
    TEXT,
    IMAGE
}

data class ContentViewState(
    val cells: List<DelegateDiffable<*>>,
    val isDownloadButtonVisible: Boolean
) {
    companion object {
        val EMPTY = ContentViewState(cells = emptyList(), isDownloadButtonVisible = false)
    }
}
