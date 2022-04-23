package com.aita.arch.dispatcher

import com.aita.arch.disposable.Disposable
import com.aita.arch.disposable.ManagedDisposable
import com.aita.arch.util.MainThreadExecutorService
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
