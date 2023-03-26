package com.example.stegomessenger.v2.common

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.setColoredText(coloredText: ColoredText) {
    val context = context ?: return

    val colorRes = coloredText.colorRes
    val startIndex = coloredText.startIndex
    val endIndex = coloredText.endIndex
    val value = coloredText.value

    val startIndexPossibleRange = value.indices
    val endIndexPossibleRange = 0..value.length
    if (colorRes == 0 ||
        startIndex !in startIndexPossibleRange ||
        endIndex !in endIndexPossibleRange ||
        startIndex > endIndex
    ) {
        text = value
        return
    }

    val spannable = SpannableString(value)
    spannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, colorRes)),
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    setText(spannable, TextView.BufferType.SPANNABLE)
}

data class ColoredText(
    val value: String,
    val startIndex: Int = 0,
    val endIndex: Int = value.length,
    @ColorRes val colorRes: Int,
)