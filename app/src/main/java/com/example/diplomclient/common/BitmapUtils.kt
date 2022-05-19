package com.example.diplomclient.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object BitmapUtils {

    fun bitmapToBase64(bitmap: Bitmap, isLossless: Boolean): String? {
        val baos = ByteArrayOutputStream()
        if (isLossless) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        }

        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.URL_SAFE)
    }

    fun base64ToBitmap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(encodedString, Base64.URL_SAFE)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            AppLogger.log(e.message.toString())
            null
        }
    }
}
