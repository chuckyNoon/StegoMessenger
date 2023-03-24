package com.example.stegomessenger.new_arch

interface IntentHandler<T> {
    fun obtainIntent(intent: T)
}