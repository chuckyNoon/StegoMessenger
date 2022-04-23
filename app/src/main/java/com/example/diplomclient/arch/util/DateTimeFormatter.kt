package com.aita.arch.di.regular

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateTimeFormatter(private val context: Context) {

    fun formatDateWithDefaultLocaleInUtc(pattern: String, millis: Long): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
        return formatter.format(millis)
    }
}
