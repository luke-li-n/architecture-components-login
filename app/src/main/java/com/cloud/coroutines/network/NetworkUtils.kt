package com.cloud.coroutines.network

import com.cloud.coroutines.data.Result
/**
 * 在try/catch中封装一个[suspend]的API调用
 * 把返回结果包装到[Result]对象中
 */
suspend fun <T> safeApiCall(block: suspend () -> ApiResponse<T>): Result<T> {
    return try {
        val result = block.invoke()
        if (result.succeeded) {
            Result.Success(result.data)
        } else {
            Result.Error(result.errorCode, result.errorMsg)
        }
    } catch (e: Exception) {
        val errorMsg = ErrorMsgFactory.create(e)
        Result.Error(errorMsg.code, errorMsg.message)
    }
}