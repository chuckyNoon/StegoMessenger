package com.example.diplomclient.arch.flux.dispatcher

import com.example.diplomclient.arch.flux.disposable.Disposable
import com.example.diplomclient.arch.flux.disposable.ManagedDisposable
import com.example.diplomclient.arch.flux.util.MainThreadExecutorService
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
