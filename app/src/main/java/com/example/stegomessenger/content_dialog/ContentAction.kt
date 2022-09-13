package com.example.stegomessenger.content_dialog

import android.graphics.Bitmap
import com.example.stegomessenger.arch.redux.Action

sealed class ContentAction : Action {
    data class Init(
        val text: String?,
        val image: Bitmap?
    ) : ContentAction()
}
