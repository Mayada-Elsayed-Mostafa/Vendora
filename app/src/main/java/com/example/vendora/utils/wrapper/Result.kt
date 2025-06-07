package com.example.vendora.utils.wrapper

sealed class Result<out T>{
    data object Loading: Result<Nothing>()
    data class Success<out T>(val data: T): Result<T>()
    data class Failure(val exception: Throwable): Result<Nothing>()
}