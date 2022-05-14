package com.example.diplomclient.common

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.Fragment

class PickImageRequest(private val requestCode: Int) {

    private val intent: Intent

    fun start(activity: Activity) {
        activity.startActivityForResult(intent, requestCode)
    }

    fun start(fragment: Fragment) {
        fragment.startActivityForResult(intent, requestCode)
    }

    init {
        val chooserTitle = "Choose image"
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        val pickIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                setType("*/*")
            }
        intent = Intent.createChooser(
            getIntent,
            chooserTitle
        ).putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
    }
}
