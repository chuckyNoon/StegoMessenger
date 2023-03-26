package com.example.stegomessenger.v2.common.model

data class TextMessageCell(
    val id: String,
    val contentText: String,
    val dateText: String,
    val isMine: Boolean,
)