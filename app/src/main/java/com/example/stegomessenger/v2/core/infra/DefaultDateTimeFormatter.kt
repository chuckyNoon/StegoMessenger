package com.example.stegomessenger.v2.core.infra

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DefaultDateTimeFormatter : DateTimeFormatter {

    override fun formatDateWithDefaultLocale(pattern: String, millis: Long): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return formatter.format(millis + TimeZone.getDefault().rawOffset)
    }
}