package com.example.stegomessenger.arch

import com.example.stegomessenger.common.network.model.ErrorResponse

sealed class Outcome<out T : Any> {

    data class Success<out T : Any>(val value: T) : Outcome<T>()

    data class Failure(val errorResponse: ErrorResponse) : Outcome<Nothing>()
}
