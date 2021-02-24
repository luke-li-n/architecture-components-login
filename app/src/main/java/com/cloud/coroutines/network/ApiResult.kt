package com.cloud.coroutines.network

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResult<T> {

    data class SuccessResult<T>(
        val data: T?,
        val errorCode: Int = -1,
        val errorMsg: String = ""
    ) : ApiResult<T>()

    data class ErrorResult<T>(val errorCode: Int, val errorMsg: String) : ApiResult<T>()


    inline fun <reified T> ApiResult<T>.onSuccess(action: (T?) -> Unit): ApiResult<T> {
        if (this is SuccessResult) action(data)
        return this
    }

    inline fun <reified T> ApiResult<T>.onError(action: (code: Int, message: String) -> Unit): ApiResult<T> {
        if (this is ErrorResult) action(errorCode, errorMsg)
        return this
    }

    companion object {
        const val SUCCESS_CODE = 0

        fun <T> failure(throwable: Throwable): ApiResult<T> {
            // 网络异常转换成业务提示
            val errorMsg = ErrorMsgFactory.create(throwable = throwable)
            return ErrorResult(errorMsg.code, errorMsg.message)
        }

        fun <T> success(response: ApiResponse<T>): ApiResult<T> {
            return if (response.succeeded) {
                // Tips 这里可以根据Code发送EventBus
                SuccessResult(response.data, response.errorCode, response.errorMsg)
            } else {
                ErrorResult(response.errorCode, response.errorMsg)
            }
        }
    }
}