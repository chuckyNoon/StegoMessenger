package com.example.diplomclient.main

import android.content.SharedPreferences
import com.example.diplomclient.common.PrefsContract
import java.util.concurrent.TimeUnit

class SyncHelper(private val prefs: SharedPreferences) {
    fun shouldSync(): Boolean {
        val lastSyncMillis = prefs.getLong(PrefsContract.LAST_SYNC, 0)
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
