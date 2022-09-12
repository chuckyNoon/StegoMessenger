package com.example.diplomclient.result

import android.graphics.Bitmap
import com.example.diplomclient.arch.redux.Action

sealed class ResultAction : Action {
    data class Init(val bitmap: Bitmap) : ResultAction()
}
