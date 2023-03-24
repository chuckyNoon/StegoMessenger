package com.example.stegomessenger.common

import com.example.stegomessenger.arch.Outcome
import com.example.stegomessenger.common.network.model.ErrorResponse
import kotlinx.coroutines.CoroutineScope
import retrofit2.HttpException

suspend fun <T: Any> safeExecute(
    block: suspend () -> T,
    onSuccess: (T) -> Unit,
    onError: (errorResponse: ErrorResponse) -> Unit
){
    val outcome = try {
        Outcome.Success(block.invoke())
    } catch (throwable: Throwable) {
        Outcome.Failure(ErrorResponse())
    }

    when (outcome) {
        is Outcome.Success -> onSuccess(outcome.value)
        is Outcome.Failure -> onError(outcome.errorResponse)
    }
}

suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> T,
    onSuccess: (T) -> Unit,
    onError: (errorResponse: ErrorResponse) -> Unit
) {

    val outcome = try {
        Outcome.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException ->
                Outcome.Failure(
                    ErrorResponse(
                        code = throwable.code(),
                        message = convertErrorBody(throwable)
                    )
                )
            else -> Outcome.Failure(ErrorResponse())
        }
    }
    when (outcome) {
        is Outcome.Success -> onSuccess(outcome.value)
        is Outcome.Failure -> onError(outcome.errorResponse)
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        val source = throwable.response()?.errorBody()?.source()?.toString()
        if (source == null) {
            null
        } else {
            val formatted = source.drop(1).dropLast(1)
            formatted.substringAfter("=")
        }
    } catch (exception: Exception) {
        null
    }
}
