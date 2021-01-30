package com.cloud.coroutines.network

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import javax.net.ssl.SSLHandshakeException

enum class ErrorMsg(val code: Int, val message: String) {
    /**
     * 系统内部错误
     */
    HTTP_SERVER_ERROR(10001, "系统内部错误"),
    HTTP_REQUEST_PARAM_ERROR(10002, "请求参数缺失或无效"),
    HTTP_INVALID_AUTH(10003, "认证信息无效或已过期"),
    HTTP_ACCESS_DENIED(10004, "无权限操作"),
    HTTP_REQUEST_INVALID(10005, "错误的请求"),
    HTTP_GET_TOKEN_ERROR(20000, "获取 IM Token 失败"),
    HTTP_SEND_CODE_OVER_FREQUENCY(20001, "发送短信请求过于频繁"),
    HTTP_SEND_CODE_FAILED(20002, "短信发送失败"),
    HTTP_SEND_CODE_INVALID_PHONE_NUMBER(20003, "手机号无效"),
    HTTP_CODE_NOT_SEND(20004, "短信验证码尚未发送"),
    HTTP_CODE_INVALID(20005, "短信验证码无效"),
    HTTP_CODE_EMPTY(20006, "验证码不能为空"),
    VERSION_EXIST(40000, "版本已存在"),
    VERSION_NO_EXIST(40001, "版本不存在"),
    NO_NEW_VERSION(40002, "没有新版本"),
    UNKNOWN_ERROR(40000, "未知错误")
}

@Suppress("unused")
interface ErrorMsgFactory {
    companion object {
        /**
         * 根据code创建错误消息
         */
        fun create(code: Int): ErrorMsg {
            for (errorMsg in ErrorMsg.values()) {
                if (errorMsg.code == code) {
                    return errorMsg
                }
            }
            return ErrorMsg.UNKNOWN_ERROR
        }

        fun create(throwable: Throwable?): ErrorMsg {
            return when (throwable) {
                is HttpException -> {
                    ErrorMsg.HTTP_ACCESS_DENIED
                }
                is UnknownHostException -> {
                    ErrorMsg.HTTP_ACCESS_DENIED
                }
                is UnknownServiceException -> {
                    ErrorMsg.HTTP_ACCESS_DENIED
                }
                is SocketTimeoutException -> {
                    ErrorMsg.HTTP_ACCESS_DENIED
                }
                is ConnectException -> {
                    ErrorMsg.HTTP_ACCESS_DENIED
                }
                is SSLHandshakeException -> {
                    ErrorMsg.HTTP_ACCESS_DENIED
                }
                else -> {
                    ErrorMsg.UNKNOWN_ERROR
                }
            }
        }
    }
}