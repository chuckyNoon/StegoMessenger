package com.example.stegomessenger.main

import android.content.SharedPreferences
import com.example.stegomessenger.common.PrefsContract
import java.util.concurrent.TimeUnit

class SyncHelper(private val prefs: SharedPreferences) {

    fun shouldSync(): Boolean {
        val lastSyncMillis = prefs.getLong(PrefsContract.LAST_SYNC, 0)
        if (prefs.getString(PrefsContract.TOKEN, null) == null) {
            return false
        }
        val currentMillis = System.currentTimeMillis()
        val secondsPassed =
            (currentMillis - lastSyncMillis) / TimeUnit.SECONDS.toMillis(SYNC_DELAY_SECONDS / 2)
        return secondsPassed > 0L
    }

    fun saveSyncTime() {
        prefs.edit().putLong(PrefsContract.LAST_SYNC, System.currentTimeMillis()).commit()
    }

    companion object {
        const val SYNC_DELAY_SECONDS = 10L
    }
}
