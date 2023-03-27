package com.example.core.util

import com.example.core.Outcome

suspend fun <T: Any> safeExecute(
    block: suspend () -> T,
    onSuccess: (T) -> Unit,
    onError: (throwable: Throwable) -> Unit
){
    val outcome = try {
        Outcome.Success(block.invoke())
    } catch (throwable: Throwable) {
        Outcome.Failure(throwable)
    }

    when (outcome) {
        is Outcome.Success -> onSuccess(outcome.value)
        is Outcome.Failure -> onError(outcome.throwable)
    }
}