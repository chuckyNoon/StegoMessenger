package com.example.stegomessenger.common

import android.content.Context
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.stegomessenger.R

object SystemUiUtils {
    fun makeStatusAndNavBarsTransparent(context: Context, window: Window) {
        val insetsController = window.getWindowInsetsControllerCompat()

        @ColorInt
        val statusBarColor = ContextCompat.getColor(context, R.color.transparent_status_bar)

        @ColorInt
        val navBarColor = ContextCompat.getColor(context, R.color.transparent_navigation_bar)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = statusBarColor
        window.navigationBarColor = navBarColor

        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = true
    }

    private fun Window.getWindowInsetsControllerCompat(): WindowInsetsControllerCompat {
        val decorView = requireNotNull(this.decorView)
        return WindowInsetsControllerCompat(this, decorView)
    }
}
