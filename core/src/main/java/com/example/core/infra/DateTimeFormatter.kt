package com.example.core.infra

interface DateTimeFormatter {
    fun formatDateWithDefaultLocale(pattern: String, millis: Long): String
}