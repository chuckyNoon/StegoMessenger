package com.example.stegomessenger.v2.common

sealed class Outcome<T> {
    data class Success<T>(val value: T): Outcome<T>()
    data class Failure(val throwable: Throwable): Outcome<Nothing>()
}