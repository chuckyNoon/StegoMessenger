package com.example.stegomessenger.arch.redux.dispatcher

import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.redux.disposable.Disposable
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
