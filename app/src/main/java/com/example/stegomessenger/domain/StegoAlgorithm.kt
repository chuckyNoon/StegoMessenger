package com.example.stegomessenger.domain

import android.graphics.Bitmap
import com.example.stegomessenger.domain.model.HiddenContent

interface StegoAlgorithm {
    fun encodeBitmap(containerBitmap: Bitmap, hiddenContent: HiddenContent): Bitmap?
    fun decodeBitmap(containerBitmap: Bitmap): HiddenContent?
}