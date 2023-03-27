package com.example.features.overview

sealed class OverviewIntent {
    data class ClickCell(val cellId: String) : OverviewIntent()
}