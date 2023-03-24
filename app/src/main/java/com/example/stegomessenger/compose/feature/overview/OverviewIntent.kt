package com.example.stegomessenger.compose.feature.overview

sealed class OverviewIntent {
    data class ClickCell(val cellId: String) : OverviewIntent()
}