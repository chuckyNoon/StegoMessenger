package com.example.diplomclient.arch.flux.dispatcher

import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.arch.flux.disposable.Disposable
import java.util.concurrent.atomic.AtomicBoolean

internal class DisposableDispatchable(
    private val dispatchable: Dispatchable,
) : Dispatchable, Disposable {

    private val isDisposedAtomic = AtomicBoolean(false)

    override fun dispatch(action: Action) {
        if (isDisposedAtomic.get()) {
            return
        }
        dispatchable.dispatch(action)
    }

    override fun dispose() {
        isDisposedAtomic.set(true)
    }
}
