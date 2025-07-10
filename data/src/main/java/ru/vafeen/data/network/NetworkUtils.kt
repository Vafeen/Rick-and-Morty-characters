package ru.vafeen.data.network

import ru.vafeen.domain.network.ResponseResult

/**
 * Wrapper function for safe network request execution with comprehensive error handling.
 *
 * This higher-order function:
 * 1. Executes the provided network request in a try-catch block
 * 2. Returns successful result wrapped in [ResponseResult.Success] if execution succeeds
 * 3. Catches all exceptions and returns them as [ResponseResult.Error]
 * 4. Logs full error stacktrace for debugging purposes
 *
 * @param T Type of the expected successful response
 * @param response Suspending lambda containing the network request logic
 * @return [ResponseResult] containing either:
 *   - [ResponseResult.Success] with the response data on successful execution
 *   - [ResponseResult.Error] with error details if any exception occurs
 */
internal suspend fun <T> getResponseResultWrappedAllErrors(response: suspend () -> T): ResponseResult<T> =
    try {
        ResponseResult.Success(data = response())
    } catch (e: Exception) {
//        Log.e(
//            "NetworkError",
//            "Request failed: ${e.javaClass.simpleName}\n" +
//                    "Message: ${e.localizedMessage}\n" +
//                    "Stacktrace: ${e.stackTraceToString()}"
//        )
        ResponseResult.Error(
            stacktrace = "${e.javaClass.simpleName}: ${e.localizedMessage}"
        )
    }