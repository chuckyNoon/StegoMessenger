package com.example.diplomclient.arch.flux.disposable

fun interface Disposable {

    fun dispose()

    companion object {
        val EMPTY: Disposable = Disposable {}
    }
}
