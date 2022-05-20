package com.example.diplomclient.koch

import android.graphics.Bitmap
import android.graphics.Matrix
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.getBit
import com.example.diplomclient.common.setBit

class Algorithm {

    fun lsbEncode(dataBitmap: Bitmap, containerBitmap: Bitmap): Bitmap? {
        AppLogger.log("Image started")
        val dataHeight = dataBitmap.height
        val dataWidth = dataBitmap.width

        val newHeight = dataHeight * 2
        val newWidth = dataWidth * 2

        val resizedContainer =
            getResizedBitmap(containerBitmap, newWidth = newWidth, newHeight = newHeight)!!

        val dataPixels = IntArray(dataWidth * dataHeight, { Int.MIN_VALUE })
        dataBitmap.getPixels(dataPixels, 0, dataWidth, 0, 0, dataWidth, dataHeight)
        dataBitmap.recycle()

        val containerPixels = IntArray(newWidth * newHeight, { Int.MIN_VALUE })
        resizedContainer.getPixels(containerPixels, 0, newWidth, 0, 0, newWidth, newHeight)
        resizedContainer.recycle()

        val originalSet = mutableSetOf<Int>()
        val resultSet = mutableSetOf<Int>()

        val currentBits = mutableListOf<Boolean>()
        for (i in 0 until newWidth) {
            for (j in 0 until newHeight) {
                val pos = i * newHeight + j
                val dataPos = pos / 4

                val valueToHide = dataPixels[dataPos]
                // originalSet.add(valueToHide)

                val bitPos1 = 0
                val bitPos2 = 1
                val bitPos3 = 8
                val bitPos4 = 9
                val bitPos5 = 16
                val bitPos6 = 17
                val bitPos7 = 24
                val bitPos8 = 25

                val offset = pos % 4
                val bitToHide1 = valueToHide.getBit(offset * 8)
                val bitToHide2 = valueToHide.getBit(offset * 8 + 1)
                val bitToHide3 = valueToHide.getBit(offset * 8 + 2)
                val bitToHide4 = valueToHide.getBit(offset * 8 + 3)
                val bitToHide5 = valueToHide.getBit(offset * 8 + 4)
                val bitToHide6 = valueToHide.getBit(offset * 8 + 5)
                val bitToHide7 = valueToHide.getBit(offset * 8 + 6)
                val bitToHide8 = valueToHide.getBit(offset * 8 + 7)

                /*if (offset == 3) {
                    resultSet.add(bitsToNum(currentBits))
                    currentBits.clear()
                }*/

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
        // AppLogger.log("cmp ${resultSet.size} ${originalSet.size}")
        resizedContainer.recycle()
        val newBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        newBitmap.setPixels(containerPixels, 0, newWidth, 0, 0, newWidth, newHeight)
        AppLogger.log("Image ended")
        return newBitmap
    }

    private fun bitsToNum(bits: List<Boolean>): Int {
        var result = 0
        for (i in bits.indices) {
            result = result.setBit(i, bits[i])
        }
        return result
    }

    fun lsbDecode(containerBitmap: Bitmap): Bitmap? {
        val containerWidth = containerBitmap.width
        val containerHeight = containerBitmap.height

        val dataWidth = containerWidth / 2
        val dataHeight = containerHeight / 2
        val dataBitmap = Bitmap.createBitmap(dataWidth, dataHeight, Bitmap.Config.ARGB_8888)

        AppLogger.log("r=$dataHeight $dataWidth")

        val dataPixels = IntArray(dataWidth * dataHeight, { 0 })
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

        for (i in 0 until dataWidth) {
            for (j in 0 until dataHeight) {
                val dataPos = i * dataHeight + j

                var newDataColor = Int.MIN_VALUE
                for (k in 0..3) {
                    val contPos = dataPos * 4 + k
                    val contValue = containerPixels[contPos]

                    val bitPos1 = 0
                    val bitPos2 = 1
                    val bitPos3 = 8
                    val bitPos4 = 9
                    val bitPos5 = 16
                    val bitPos6 = 17
                    val bitPos7 = 24
                    val bitPos8 = 25

                    val offset = k * 8
                    newDataColor = newDataColor
                        .setBit(offset, contValue.getBit(bitPos1))
                        .setBit(offset + 1, contValue.getBit(bitPos2))
                        .setBit(offset + 2, contValue.getBit(bitPos3))
                        .setBit(offset + 3, contValue.getBit(bitPos4))
                        .setBit(offset + 4, contValue.getBit(bitPos5))
                        .setBit(offset + 5, contValue.getBit(bitPos6))
                        .setBit(offset + 6, contValue.getBit(bitPos7))
                        .setBit(offset + 7, contValue.getBit(bitPos8))
                }

                dataPixels[dataPos] = newDataColor
            }
        }
        dataBitmap.setPixels(dataPixels, 0, dataWidth, 0, 0, dataWidth, dataHeight)
        AppLogger.log("Image endedr")
        AppLogger.log(dataPixels.take(20).toString())
        return dataBitmap
    }

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }
}
