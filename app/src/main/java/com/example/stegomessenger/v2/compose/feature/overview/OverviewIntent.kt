package com.example.stegomessenger.v2.compose.feature.overview

sealed class OverviewIntent {
    data class ClickCell(val cellId: String) : OverviewIntent()
}