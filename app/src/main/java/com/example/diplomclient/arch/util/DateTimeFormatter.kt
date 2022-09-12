package com.example.diplomclient.arch.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateTimeFormatter {

    fun formatDateWithDefaultLocale(pattern: String, millis: Long): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return formatter.format(millis + TimeZone.getDefault().rawOffset)
    }
}
