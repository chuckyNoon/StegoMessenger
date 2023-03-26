package com.example.stegomessenger.v2.main

import com.example.stegomessenger.v2.common.PrefsContract
import com.example.stegomessenger.v2.common.infra.Prefs
import java.util.concurrent.TimeUnit

class SyncHelper(private val prefs: Prefs) {

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

    fun saveSyncTime() =
        prefs.saveLong(PrefsContract.LAST_SYNC, System.currentTimeMillis())

    companion object {
        const val SYNC_DELAY_SECONDS = 10L
    }
}
