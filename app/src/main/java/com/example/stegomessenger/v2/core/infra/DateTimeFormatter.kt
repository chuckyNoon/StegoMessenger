package com.example.stegomessenger.v2.core.infra

interface DateTimeFormatter {
    fun formatDateWithDefaultLocale(pattern: String, millis: Long): String
}