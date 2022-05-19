package com.example.diplomclient.result

import android.graphics.Bitmap

data class ResultState(
    val bitmap: Bitmap?,
    val viewState: ResultViewState
) {
    companion object {
        val EMPTY =
            ResultState(
                bitmap = null,
                viewState = ResultViewState.EMPTY
            )
    }
}

data class ResultViewState(
    val bitmap: Bitmap?
) {
    companion object {
        val EMPTY = ResultViewState(
            bitmap = null
        )
    }
}
