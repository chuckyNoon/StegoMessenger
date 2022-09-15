package com.example.stegomessenger.arch.util

import androidx.annotation.StringRes

interface StringsProvider {
    fun getString(@StringRes stringId: Int): String
}