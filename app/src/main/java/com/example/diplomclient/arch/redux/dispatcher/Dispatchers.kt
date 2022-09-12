package com.example.diplomclient.arch.redux.dispatcher

import com.example.diplomclient.arch.redux.disposable.Disposable
import com.example.diplomclient.arch.redux.disposable.ManagedDisposable
import com.example.diplomclient.arch.redux.util.MainThreadExecutorService
import java.util.concurrent.Executors

object Dispatchers {

    fun newSession(): Session {
        val dispatcher = Dispatcher(
            Executors.newSingleThreadExecutor(),
            MainThreadExecutorService()
        )
        val shutdownDisposable = ManagedDisposable { dispatcher.shutdown() }
        return Session(dispatcher, shutdownDisposable)
    }

    data class Session internal constructor(
        val dispatcher: Dispatcher,
        val shutdownDisposable: Disposable,
    )
}
