package com.example.stegomessenger.common

import android.content.Intent
import android.provider.MediaStore


fun imageChooserIntent(): Intent =
    Intent.createChooser(
        Intent(Intent.ACTION_GET_CONTENT),
        "Choose image"
    ).putExtra(
        Intent.EXTRA_INITIAL_INTENTS,
        arrayOf(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "*/*"
            }
        )
    )