package com.example.diplomclient.koch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.getBit
import com.example.diplomclient.common.setBit
import java.io.ByteArrayOutputStream
import kotlin.math.sqrt

class Algorithm {

    fun lsbEncode(
        dataBitmap: Bitmap,
        containerBitmap: Bitmap
    ): Bitmap? {
        AppLogger.log("Image started")
        val stream = ByteArrayOutputStream()

        dataBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val dataByteArray: ByteArray = stream.toByteArray()
        AppLogger.log("compr=${dataByteArray.size} - ${dataBitmap.width * dataBitmap.height}")
        encodedContentByteArray = dataByteArray
        AppLogger.log("hash1=${dataByteArray.contentHashCode()}")

        val containerHeight = containerBitmap.height
        val containerWidth = containerBitmap.width

        val estimatedDataLength = dataByteArray.size + LABEL_START.length + LABEL_END.length
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

        val resizedContainer1 = if (scaleSide > 1) {
            getResizedBitmap(
                containerBitmap,
                newWidth = newContainerWidth,
                newHeight = newContainerHeight
            )!!
        } else {
            containerBitmap
        }
        val containerPixels1 = IntArray(newContainerWidth * newContainerHeight) { 0}
        resizedContainer1.getPixels(
            containerPixels1,
            0,
            newContainerWidth,
            0,
            0,
            newContainerWidth,
            newContainerHeight
        )

        val resizedContainer =
            Bitmap.createBitmap(newContainerWidth, newContainerHeight, Bitmap.Config.ARGB_8888)
        resizedContainer.setPixels(
            containerPixels1,
            0,
            newContainerWidth,
            0,
            0,
            newContainerWidth,
            newContainerHeight
        )
        val containerPixels = IntArray(newContainerWidth * newContainerHeight) {0 }
        resizedContainer.getPixels(
            containerPixels,
            0,
            newContainerWidth,
            0,
            0,
            newContainerWidth,
            newContainerHeight
        )

        val labelStartBytes = LABEL_START.map { it.code.toByte() }
        val labelEndBytes = LABEL_END.map { it.code.toByte() }

        val messageBytes = ByteArray(estimatedDataLength)

        labelStartBytes.forEachIndexed { index, value ->
            messageBytes[index] = value
        }

        AppLogger.log("source form")
        dataByteArray.forEachIndexed { index, byte ->
            if (index <= 10 || index > dataByteArray.size - 10) {
                AppLogger.log(byte.toInt().toString())
            }
            messageBytes[index + labelStartBytes.size] = byte
        }

        labelEndBytes.forEachIndexed { index, value ->
            messageBytes[LABEL_START.length + dataByteArray.size + index] = value
        }
        encodedMessageByteArray = messageBytes

        AppLogger.log("s = $scaleK / $scaleSide d=${newContainerWidth * newContainerHeight} e=$estimatedDataLength")
        messageBytes.take(10).forEach {
            AppLogger.log(it.toChar().toString())
        }
        AppLogger.log("wr")

        for (i in 0 until newContainerWidth) {
            for (j in 0 until newContainerHeight) {
                val pos = i * newContainerHeight + j
                val dataPos = pos

                if (dataPos >= estimatedDataLength) {
                    break
                }

                val valueToHide = messageBytes[dataPos]
                // originalSet.add(valueToHide)

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
        // AppLogger.log("cmp ${resultSet.size} ${originalSet.size}")
        oldPixels = containerPixels
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

        val e = lsbDecode(newBitmap)
        AppLogger.log("Image ended ${e?.height}- ${newBitmap.width}")
/*
        val outStream = ByteArrayOutputStream()
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)

        val outBytes = outStream.toByteArray()
        val outBmp = BitmapFactory.decodeByteArray(outBytes, 0, outBytes.size)
        val outPixels = IntArray(outBmp.width * outBmp.height) { Int.MIN_VALUE }
        outBmp.getPixels(
            outPixels,
            0,
            outBmp.width,
            0,
            0,
            outBmp.width,
            outBmp.height
        )

        AppLogger.log("cont_si = ${outPixels.size}/ ${containerPixels.size}")
        for (i in outPixels.indices) {
            val a = outPixels[i]
            val b = containerPixels.getOrNull(i)

            if (a != b) {
                AppLogger.log("i = $i a = $a b= $b")
            }
        }
        return null

        //val f = lsbDecode(outBmp)
        //AppLogger.log("${f == null}")*/
        return newBitmap
    }

    fun test(bmp: Bitmap) {
        val containerWidth = bmp.width
        val containerHeight = bmp.height
        AppLogger.log("Decode start $containerHeight - $containerWidth")

        val pixels1 = IntArray(containerWidth * containerHeight, { 0 })
        bmp.getPixels(
            pixels1,
            0,
            containerWidth,
            0,
            0,
            containerWidth,
            containerHeight
        )

        for (i in pixels1.indices) {
            pixels1[i] = pixels1[i] + 1
        }

        val bmp2 =
            Bitmap.createBitmap(containerWidth, containerHeight, Bitmap.Config.ARGB_8888)
        bmp2.setPixels(
            pixels1,
            0,
            containerWidth,
            0,
            0,
            containerWidth,
            containerHeight
        )

        val pixels2 = IntArray(containerWidth * containerHeight, { 0 })
        bmp2.getPixels(
            pixels2,
            0,
            containerWidth,
            0,
            0,
            containerWidth,
            containerHeight
        )

        val bmp3 =
            Bitmap.createBitmap(containerWidth, containerHeight, Bitmap.Config.ARGB_8888)
        bmp3.setPixels(
            pixels2,
            0,
            containerWidth,
            0,
            0,
            containerWidth,
            containerHeight
        )
        val pixels3 = IntArray(containerWidth * containerHeight, { 0 })
        bmp3.getPixels(
            pixels3,
            0,
            containerWidth,
            0,
            0,
            containerWidth,
            containerHeight
        )
        AppLogger.log("f1")
        pixels3.forEachIndexed { index, i ->
            val a = i
            val b = pixels2[index]

            if (a != b) {
                AppLogger.log("$index $a $b")
            } else{
                AppLogger.log("f")
            }
        }
        AppLogger.log("f2")
    }

    fun lsbDecode(containerBitmap: Bitmap): Bitmap? {
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
        containerPixels.forEachIndexed { index, v ->
            if (oldPixels!!.get(index) != v) {
                AppLogger.log("bbb ${oldPixels!!.get(index)} / $v")
            }
        }

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
        AppLogger.log("qqqqq" + messageStringBuilder.substring(0, 30).toString())
        val hiddenStartIndex = messageStringBuilder.indexOf(LABEL_START) + LABEL_START.length
        val hiddenEndIndex = messageStringBuilder.indexOf(LABEL_END) - 1
        AppLogger.log("$hiddenStartIndex $hiddenEndIndex")
        if (hiddenStartIndex < 0 && hiddenEndIndex < 0) {
            AppLogger.log("nout found")
            return null
        }
        AppLogger.log("extra form")
        val contentBytes = messageStringBuilder.mapIndexedNotNull { index, c ->
            if (index in hiddenStartIndex..hiddenEndIndex) {
                c.toByte()
            } else {
                null
            }
        }.toByteArray()
        compare(content = contentBytes, message = messageBytes)
        AppLogger.log(
            (contentBytes.take(10) + contentBytes.takeLast(10)).map {
                it.toInt().toString()
            }.toString()
        )
        AppLogger.log("hash2=${contentBytes.contentHashCode()}")

        val bmp = BitmapFactory.decodeByteArray(
            contentBytes,
            0,
            contentBytes.size
        )

        return bmp/*

        val dataBitmap = Bitmap.createBitmap(dataWidth, dataHeight, Bitmap.Config.ARGB_8888)
        dataBitmap.setPixels(dataPixels, 0, dataWidth, 0, 0, dataWidth, dataHeight)
        AppLogger.log("Image endedr")
        AppLogger.log(dataPixels.take(20).toString())
        return dataBitmap*/
    }

    private fun compare(content: ByteArray, message: ByteArray) {
        val oldContent = requireNotNull(encodedContentByteArray)
        val oldMessage = requireNotNull(encodedMessageByteArray)
        AppLogger.log("cmp")
        AppLogger.log("con = ${oldContent.size} - ${content.size}")
        AppLogger.log("mes = ${oldMessage.size} - ${message.size}")

        var ok = 0
        var ne_ok = 0
        for (i in content.indices) {
            val a = content[i]
            val b = oldContent.getOrNull(i)

            if (a != b) {
                ne_ok += 1
                // AppLogger.log("i = $i a = $a b= $b")
            } else {
                ok += 1
            }
        }
        AppLogger.log("ok = $ok neok = $ne_ok")
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

    companion object {
        val LABEL_START = "e1wfw"
        val LABEL_END = "erver32c"

        var encodedMessageByteArray: ByteArray? = null
        var encodedContentByteArray: ByteArray? = null

        var oldPixels: IntArray? = null
    }
}
