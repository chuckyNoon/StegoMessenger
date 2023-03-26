package com.example.stegomessenger.v2.core.arch

interface IntentHandler<T> {
    fun obtainIntent(intent: T)
}