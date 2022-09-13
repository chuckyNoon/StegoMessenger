package com.example.stegomessenger.arch.redux.disposable

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
