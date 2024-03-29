package com.example.stegomessenger.arch.util

interface Prefs {

    fun saveString(key: String, value: String?): Boolean

    fun saveLong(key: String, value: Long) : Boolean

    fun getString(key: String, fallbackValue: String?): String?

    fun getLong(key: String, fallbackValue: Long): Long
}