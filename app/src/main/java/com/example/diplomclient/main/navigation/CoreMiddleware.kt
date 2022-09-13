package com.example.diplomclient.main.navigation

import android.os.Handler
import android.os.Looper
import com.example.diplomclient.arch.redux.dispatcher.Dispatchable
import com.example.diplomclient.arch.redux.store.Middleware
import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.main.SyncHelper
import com.example.diplomclient.overview.OverviewAction
import java.util.concurrent.TimeUnit

class CoreMiddleware(
    private val apiHelper: ApiHelper,
    private val syncHelper: SyncHelper
) : Middleware<CoreNavState> {

    private val handler = Handler(Looper.getMainLooper())

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: CoreNavState,
        newState: CoreNavState
    ) {
        when (action) {
            CoreAction.ReloadChats -> loadChats(dispatchable, isForced = true)
            CoreAction.SyncWithServer -> {
                if (syncHelper.shouldSync()) {
                    loadChats(dispatchable, isForced = false)
                }
                handler.postDelayed(
                    {
                        dispatchable.dispatch(CoreAction.SyncWithServer)
                    },
                    TimeUnit.SECONDS.toMillis(SyncHelper.SYNC_DELAY_SECONDS)
                )
            }
        }
    }

    private fun loadChats(dispatchable: Dispatchable, isForced: Boolean) {
        syncHelper.saveSyncTime()

        dispatchable.dispatch(OverviewAction.ChatsLoadingStarted)
        launchBackgroundWork {
            safeApiCall(
                apiCall = { apiHelper.getChats(isForced) },
                onSuccess = {
                    dispatchable.dispatch(CoreAction.ChatsReloaded(it.chats))
                },
                onError = {}
            )
        }
    }
}
