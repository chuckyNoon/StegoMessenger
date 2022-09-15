package com.example.stegomessenger.arch.util

interface DateTimeFormatter {
    fun formatDateWithDefaultLocale(pattern: String, millis: Long): String
}