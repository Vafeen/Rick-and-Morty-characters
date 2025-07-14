package ru.vafeen.domain.network

/**
 * A sealed class representing the result of a network operation.
 * Enforces explicit handling of success/error cases at compile time.
 *
 * @param T Type of the successful result data
 *
 * Usage example:
 * ```
 * when (result) {
 *     is ResponseResult.Success -> processData(result.data)
 *     is ResponseResult.Error -> showError(result.stacktrace)
 * }
 * ```
 */
sealed class ResponseResult<out T> {

    /**
     * Represents a successful network operation with parsed data.
     *
     * @property data The successfully retrieved data of type [T]
     * @param T Type of the contained data
     */
    data class Success<out T>(val data: T) : ResponseResult<T>()

    /**
     * Represents a failed network operation with error details.
     *
     * @property stacktrace String representation of the error stacktrace.
     *                     Typically includes exception details and call stack.
     */
    data class Error(val stacktrace: String) : ResponseResult<Nothing>()
}