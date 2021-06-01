package com.cloud.coroutines.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T> {

    data class Success<out T>(val data: T?) : Result<T>()
    data class Error(val code: Int, val message: String) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$message]"
        }
    }
}

inline fun <reified T> Result<T>.success(action: (T?) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <reified T> Result<T>.error(action: (message: String) -> Unit): Result<T> {
    if (this is Result.Error) action(this.message)
    return this
}