package com.cloud.coroutines.network

data class ApiResponse<T>(
    val data: T?,
    val errorCode: Int = -1,
    val errorMsg: String = ""
) {
    val succeeded
        get() = SUCCESS_CODE == errorCode

    companion object {
        private const val SUCCESS_CODE = 0
    }
}
