package com.example.diplomclient.arch.redux.util

import androidx.annotation.CheckResult

data class Event<T>(private val value: T?) {

    private var isRead = false

    @CheckResult
    fun readValue(): T? {
        if (isRead) {
            return null
        }
        isRead = true
        return value
    }
}
