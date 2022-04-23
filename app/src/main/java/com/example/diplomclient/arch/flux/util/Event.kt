package com.aita.arch.util

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

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as Event<*>

        if (value != other.value) {
            return false
        }
        if (isRead != other.isRead) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + isRead.hashCode()
        return result
    }

    override fun toString(): String =
        "Event(value=$value, isRead=$isRead)"
}
