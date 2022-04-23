package com.aita.arch.di.regular

import android.content.Context

class StringsProvider(appContext: Context) {

    private val resources = appContext.resources!!

    fun getString(stringResId: Int): String = resources.getString(stringResId)

    fun getString(stringResId: Int, vararg formatArgs: Any): String =
        resources.getString(stringResId, *formatArgs)

    fun getPlural(pluralResId: Int, count: Int): String =
        resources.getQuantityString(pluralResId, count)

    fun getPluralFormatted(pluralResId: Int, count: Int): String =
        resources.getQuantityString(pluralResId, count, count)

    fun getStringArray(stringArrayResId: Int): Array<String> =
        resources.getStringArray(stringArrayResId)
}
