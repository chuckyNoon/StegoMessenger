package com.example.stegomessenger.v2.new_arch

interface IntentHandler<T> {
    fun obtainIntent(intent: T)
}