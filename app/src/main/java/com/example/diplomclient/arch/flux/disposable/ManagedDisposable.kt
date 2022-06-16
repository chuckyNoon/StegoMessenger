package com.example.diplomclient.arch.flux.disposable

import com.example.diplomclient.arch.flux.disposable.Disposable

class ManagedDisposable(private val disposable: Disposable) : Disposable {

    private var isDisposed: Boolean = false

    override fun dispose() {
        if (isDisposed) {
            return
        }
        disposable.dispose()
        isDisposed = true
    }
}
