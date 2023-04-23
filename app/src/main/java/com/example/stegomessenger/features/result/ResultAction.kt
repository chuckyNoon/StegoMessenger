package com.example.stegomessenger.features.result

import android.graphics.Bitmap
import com.example.stegomessenger.arch.redux.Action

sealed class ResultAction : Action {
    data class Init(val bitmap: Bitmap) : ResultAction()
}
