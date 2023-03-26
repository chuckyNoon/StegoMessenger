package com.example.stegomessenger.v2.compose.model

data class TextMessageCell(
    val id: String,
    val contentText: String,
    val dateText: String,
    val isMine: Boolean,
)