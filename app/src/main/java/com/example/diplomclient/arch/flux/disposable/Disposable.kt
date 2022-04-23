package com.aita.arch.disposable

fun interface Disposable {

    fun dispose()

    companion object {
        val EMPTY: Disposable = Disposable {}
    }
}
