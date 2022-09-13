package com.example.stegomessenger.arch.redux.util

import androidx.annotation.CheckResult

class Event<T>(private val value: T?) {

    private var isRead = false

    @CheckResult
    fun readValue(): T? {
        if (isRead) {
            return null
        }
        isRead = true
        return value
    }

    override fun toString(): String =
        "Event(value=$value, isRead=$isRead)"
}
