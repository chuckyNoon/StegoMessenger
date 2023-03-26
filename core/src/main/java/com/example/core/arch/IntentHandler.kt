package com.example.core.arch

interface IntentHandler<T> {
    fun obtainIntent(intent: T)
}