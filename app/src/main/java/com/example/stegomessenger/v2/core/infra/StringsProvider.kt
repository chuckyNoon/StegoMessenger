package com.example.stegomessenger.v2.core.infra

import androidx.annotation.StringRes

interface StringsProvider {
    fun getString(@StringRes stringId: Int): String
}