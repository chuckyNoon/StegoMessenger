package com.example.stegomessenger.app.core

import android.os.Handler
import android.os.Looper
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.data.chat.ChatsRepository
import com.example.stegomessenger.app.SyncHelper
import com.example.stegomessenger.features.overview.OverviewAction
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CoreMiddleware @Inject constructor(
    private val chatRepository: ChatsRepository,
    private val syncHelper: SyncHelper
) : Middleware<CoreNavState>() {

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
        middlewareScope.launch {
            runCatching { chatRepository.getChats(isForced) }
                .onSuccess { dispatchable.dispatch(CoreAction.ChatsReloaded(it)) }
        }
    }
}
