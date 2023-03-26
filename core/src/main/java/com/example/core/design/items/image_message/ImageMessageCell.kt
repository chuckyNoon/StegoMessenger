package com.example.core.design.items.image_message

import android.graphics.Bitmap

data class ImageMessageCell(
    val id: String,
    val imageSource: ImageSource,
    val dateText: String,
    val isMine: Boolean,
    val isInProgress: Boolean
)  {

    sealed class ImageSource {
        data class Url(val url: String) : ImageSource()
        data class LoadedBitmap(val bitmap: Bitmap) : ImageSource()
    }

}
