package com.example.stegomessenger.v2.arch

interface IntentHandler<T> {
    fun obtainIntent(intent: T)
}