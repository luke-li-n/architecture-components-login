package com.cloud.coroutines.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T?) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()


    inline fun <reified T : Any> Result<T>.onSuccess(action: (T?) -> Unit): Result<T> {
        if (this is Result.Success) action(data)
        return this
    }

    inline fun <reified T : Any> Result<T>.onError(action: (message: String) -> Unit): Result<T> {
        if (this is Result.Error) action(this.message)
        return this
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$message]"
        }
    }
}