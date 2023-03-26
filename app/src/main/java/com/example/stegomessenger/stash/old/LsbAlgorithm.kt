package com.example.stegomessenger.stash.old

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.example.core.AppLogger
import com.example.core.util.getBit
import com.example.core.util.setBit
import kotlin.math.sqrt

// TODO: refactor and optimize logic
class LsbAlgorithm {

    fun lsbEncode(
        dataByteArray: ByteArray,
        containerBitmap: Bitmap,
        startLabel: String,
        endLabel: String
    ): Bitmap? {
        val containerHeight = containerBitmap.height
        val containerWidth = containerBitmap.width

        val estimatedDataLength = dataByteArray.size + startLabel.length + endLabel.length
        val containerLength = containerHeight * containerWidth

        val scaleK = estimatedDataLength.toDouble() / containerLength
        val scaleSide = sqrt(scaleK)

        val newContainerHeight: Int
        val newContainerWidth: Int
        if (scaleSide > 1f) {
            newContainerHeight = (containerHeight * scaleSide).toInt() + 1
            newContainerWidth = (containerWidth * scaleSide).toInt() + 1
        } else {
            newContainerHeight = containerHeight
            newContainerWidth = containerWidth
        }

        val resizedContainer = if (scaleSide > 1) {
            val resized = getResizedBitmap(
                containerBitmap,
                newWidth = newContainerWidth,
                newHeight = newContainerHeight
            )!!
            containerBitmap.recycle()
            resized
        } else {
            containerBitmap
        }
        val containerPixels = IntArray(newContainerWidth * newContainerHeight) { 0 }
        resizedContainer.getPixels(
            containerPixels,
            0,
            newContainerWidth,
            0,
            0,
            newContainerWidth,
            newContainerHeight
        )
        resizedContainer.recycle()

        val labelStartBytes = startLabel.map { it.code.toByte() }
        val labelEndBytes = endLabel.map { it.code.toByte() }

        val messageBytes = ByteArray(estimatedDataLength)

        labelStartBytes.forEachIndexed { index, value ->
            messageBytes[index] = value
        }

        dataByteArray.forEachIndexed { index, byte ->
            messageBytes[index + labelStartBytes.size] = byte
        }

        labelEndBytes.forEachIndexed { index, value ->
            messageBytes[startLabel.length + dataByteArray.size + index] = value
        }

        for (i in 0 until newContainerWidth) {
            for (j in 0 until newContainerHeight) {
                val pos = i * newContainerHeight + j

                if (pos >= estimatedDataLength) {
                    break
                }

                val valueToHide = messageBytes[pos]

                val bitPos1 = 0
                val bitPos2 = 1
                val bitPos3 = 8
                val bitPos4 = 9
                val bitPos5 = 16
                val bitPos6 = 17
                val bitPos7 = 18
                val bitPos8 = 19

                val offset = 0
                val bitToHide1 = valueToHide.getBit(offset * 8)
                val bitToHide2 = valueToHide.getBit(offset * 8 + 1)
                val bitToHide3 = valueToHide.getBit(offset * 8 + 2)
                val bitToHide4 = valueToHide.getBit(offset * 8 + 3)
                val bitToHide5 = valueToHide.getBit(offset * 8 + 4)
                val bitToHide6 = valueToHide.getBit(offset * 8 + 5)
                val bitToHide7 = valueToHide.getBit(offset * 8 + 6)
                val bitToHide8 = valueToHide.getBit(offset * 8 + 7)

                val containerColor = containerPixels[pos]

                val updatedColor = containerColor
                    .setBit(bitPos1, bitToHide1)
                    .setBit(bitPos2, bitToHide2)
                    .setBit(bitPos3, bitToHide3)
                    .setBit(bitPos4, bitToHide4)
                    .setBit(bitPos5, bitToHide5)
                    .setBit(bitPos6, bitToHide6)
                    .setBit(bitPos7, bitToHide7)
                    .setBit(bitPos8, bitToHide8)

                containerPixels[pos] = updatedColor
            }
        }

        val newBitmap =
            Bitmap.createBitmap(newContainerWidth, newContainerHeight, Bitmap.Config.ARGB_8888)
        newBitmap.setPixels(
            containerPixels,
            0,
            newContainerWidth,
            0,
            0,
            newContainerWidth,
            newContainerHeight
        )

        return newBitmap
    }

    fun lsbDecode(containerBitmap: Bitmap): DecodeResult? {
        val containerWidth = containerBitmap.width
        val containerHeight = containerBitmap.height
        AppLogger.log("Decode start $containerHeight - $containerWidth")

        val containerPixels = IntArray(containerWidth * containerHeight, { 0 })
        containerBitmap.getPixels(
            containerPixels,
            0,
            containerWidth,
            0,
            0,
            containerWidth,
            containerHeight
        )
        val messageBytes = ByteArray(containerWidth * containerHeight, { 0 })

        for (i in containerPixels.indices) {
            val pixel = containerPixels[i]
            var hiddenMessageByte = Byte.MIN_VALUE

            val bitPos1 = 0
            val bitPos2 = 1
            val bitPos3 = 8
            val bitPos4 = 9
            val bitPos5 = 16
            val bitPos6 = 17
            val bitPos7 = 18
            val bitPos8 = 19

            hiddenMessageByte = hiddenMessageByte
                .setBit(0, pixel.getBit(bitPos1))
                .setBit(1, pixel.getBit(bitPos2))
                .setBit(2, pixel.getBit(bitPos3))
                .setBit(3, pixel.getBit(bitPos4))
                .setBit(4, pixel.getBit(bitPos5))
                .setBit(5, pixel.getBit(bitPos6))
                .setBit(6, pixel.getBit(bitPos7))
                .setBit(7, pixel.getBit(bitPos8))

            messageBytes[i] = hiddenMessageByte
        }

        val messageChars = messageBytes.map { it.toChar() }

        val messageStringBuilder = StringBuilder(messageChars.size)
        messageChars.forEach {
            messageStringBuilder.append(it)
        }
        return tryToParseImage(messageStringBuilder) ?: tryToParseText(messageStringBuilder)
    }

    private fun tryToParseImage(messageStringBuilder: StringBuilder): DecodeResult.Image? {
        val hiddenStartIndex =
            messageStringBuilder.indexOf(LABEL_START_IMG) + LABEL_START_IMG.length
        val hiddenEndIndex = messageStringBuilder.indexOf(LABEL_END_IMG) - 1

        if (hiddenStartIndex < 0 || hiddenEndIndex < 0) {
            return null
        }

        val contentBytes = messageStringBuilder.mapIndexedNotNull { index, c ->
            if (index in hiddenStartIndex..hiddenEndIndex) {
                c.toByte()
            } else {
                null
            }
        }.toByteArray()

        return DecodeResult.Image(
            bitmap = BitmapFactory.decodeByteArray(
                contentBytes,
                0,
                contentBytes.size
            )
        )
    }

    private fun tryToParseText(messageStringBuilder: StringBuilder): DecodeResult.Text? {
        val hiddenStartIndex =
            messageStringBuilder.indexOf(LABEL_START_TEXT) + LABEL_START_TEXT.length
        val hiddenEndIndex = messageStringBuilder.indexOf(LABEL_END_TEXT)

        if (hiddenStartIndex < 0 || hiddenEndIndex < 0) {
            return null
        }

        return DecodeResult.Text(
            text = messageStringBuilder.substring(hiddenStartIndex, hiddenEndIndex)
        )
    }

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        val matrix = Matrix()

        matrix.postScale(scaleWidth, scaleHeight)

        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

    sealed class DecodeResult {
        data class Image(val bitmap: Bitmap) : DecodeResult()
        data class Text(val text: String) : DecodeResult()
    }

    companion object {
        // TODO: generate labels from user data
        const val LABEL_START_IMG = "e1wfw"
        const val LABEL_END_IMG = "erver32c"

        const val LABEL_START_TEXT = "rew3434czx"
        const val LABEL_END_TEXT = "34mf3.f/f34"
    }
}
