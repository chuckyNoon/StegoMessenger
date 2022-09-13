package com.example.stegomessenger.arch.redux.disposable

fun interface Disposable {

    fun dispose()

    companion object {
        val EMPTY: Disposable = Disposable {}
    }
}
