package com.example.stegomessenger.arch.redux.dispatcher

import com.example.stegomessenger.arch.redux.disposable.Disposable
import com.example.stegomessenger.arch.redux.disposable.ManagedDisposable
import com.example.stegomessenger.arch.redux.util.MainThreadExecutorService
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
