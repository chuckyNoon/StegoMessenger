package com.example.stegomessenger.v2.common.infra

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class DefaultPrefs(context: Context) : Prefs {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("prefs", MODE_PRIVATE)

    override fun saveString(key: String, value: String?): Boolean =
        getEditor().putString(key, value).commit()

    override fun saveLong(key: String, value: Long): Boolean =
        getEditor().putLong(key, value).commit()

    override fun getString(key: String, fallbackValue: String?): String? =
        sharedPreferences.getString(key, fallbackValue)

    override fun getLong(key: String, fallbackValue: Long): Long =
        sharedPreferences.getLong(key, fallbackValue)

    private fun getEditor() = sharedPreferences.edit()
}