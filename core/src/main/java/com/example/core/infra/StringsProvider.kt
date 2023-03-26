package com.example.core.infra

import androidx.annotation.StringRes

interface StringsProvider {
    fun getString(@StringRes stringId: Int): String
}