package com.example.diplomclient.common

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.diplomclient.main.MainApplication

object PrefsHelper {

    fun getPrefs(): SharedPreferences =
        MainApplication.getInstance().getSharedPreferences("prefs", MODE_PRIVATE)

    fun getEditor(): SharedPreferences.Editor = getPrefs().edit()
}
