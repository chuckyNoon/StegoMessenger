package com.example.stegomessenger.arch.util

import android.content.Context
import androidx.annotation.StringRes

class StringsProvider(private val context: Context) {

    fun getString(@StringRes stringId: Int): String =
        context.getString(stringId)
}