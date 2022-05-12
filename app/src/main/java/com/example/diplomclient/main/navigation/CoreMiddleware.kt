package com.example.diplomclient.main.navigation

import android.os.Handler
import android.os.Looper
import com.aita.arch.dispatcher.Dispatchable
import com.aita.arch.store.Middleware
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.network.ApiHelper
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.common.safeApiCall
import com.example.diplomclient.overview.OverviewAction

class CoreMiddleware(
    private val apiHelper: ApiHelper
) : Middleware<CoreNavState> {

    private val handler = Handler(Looper.getMainLooper())

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: CoreNavState,
        newState: CoreNavState
    ) {
        when (action) {
            CoreAction.ReloadChats -> loadChats(dispatchable)
        }
    }

    private fun loadChats(dispatchable: Dispatchable) {
        dispatchable.dispatch(OverviewAction.ChatsLoadingStarted)
        AppLogger.log("chats st")
        launchBackgroundWork {
            safeApiCall(
                apiCall = { apiHelper.getChats() },
                onSuccess = {
                    AppLogger.log("chats scu")
                    dispatchable.dispatch(CoreAction.ChatsReloaded(it.chats))
                },
                onError = {
                    AppLogger.log("chats f")
                    dispatchable.dispatch(CoreAction.ShowError(it.message ?: "f2"))
                }
            )
        }
    }
}
