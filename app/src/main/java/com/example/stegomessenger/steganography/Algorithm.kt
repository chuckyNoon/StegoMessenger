package com.example.stegomessenger.steganography

import android.graphics.Bitmap

interface Algorithm {
    fun encode(
        dataByteArray: ByteArray,
        containerBitmap: Bitmap,
        startLabel: String,
        endLabel: String
    ): Bitmap?

    fun decode(containerBitmap: Bitmap): LsbAlgorithm.DecodeResult
}