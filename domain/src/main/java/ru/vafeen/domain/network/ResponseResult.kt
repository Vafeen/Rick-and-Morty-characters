package ru.vafeen.domain.network

sealed class ResponseResult<out T> {
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val stacktrace: String) : ResponseResult<Nothing>()
}