package com.example.stegomessenger.v2.common.infra

interface DateTimeFormatter {
    fun formatDateWithDefaultLocale(pattern: String, millis: Long): String
}