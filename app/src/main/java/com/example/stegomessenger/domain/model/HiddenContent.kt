package com.example.stegomessenger.domain.model

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

sealed class HiddenContent {
    abstract fun toByteArray(): ByteArray

    data class Image(val bitmap: Bitmap) : HiddenContent() {
        override fun toByteArray(): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            stream.close()
            return byteArray
        }
    }

    data class Text(val text: String) : HiddenContent() {
        override fun toByteArray(): ByteArray {
            return text.toByteArray(Charsets.UTF_8)
        }
    }
}