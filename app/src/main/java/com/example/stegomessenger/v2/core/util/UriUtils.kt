package com.example.stegomessenger.v2.core.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun Uri.getName(context: Context): String? {
    val returnCursor = context.contentResolver.query(this, null, null, null, null) ?: return null
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

    returnCursor.moveToFirst()
    val fileName = returnCursor.getString(nameIndex)
    returnCursor.close()

    return fileName
}
