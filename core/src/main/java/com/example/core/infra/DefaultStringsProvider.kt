package com.example.core.infra

import android.content.Context
import androidx.annotation.StringRes

class DefaultStringsProvider(private val context: Context) : StringsProvider {

    override fun getString(@StringRes stringId: Int): String =
        context.getString(stringId)
}