package com.example.stegomessenger.v2.common.infra

import android.content.Context
import androidx.annotation.StringRes

class DefaultStringsProvider(private val context: Context) : StringsProvider {

    override fun getString(@StringRes stringId: Int): String =
        context.getString(stringId)
}