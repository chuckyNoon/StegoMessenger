package com.example.stegomessenger.v2.features.overview

sealed class OverviewIntent {
    data class ClickCell(val cellId: String) : OverviewIntent()
}