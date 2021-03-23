package com.cloud.coroutines.network

import android.nfc.FormatException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

enum class ErrorMsg(val code: Int, val message: String) {
    UNAUTHORIZED(401, "未授权的请求"),//  未授权的请求
    FORBIDDEN(403, "禁止访问"),//禁止访问
    NOT_FOUND(404, "服务器地址未找到"),//服务器地址未找到
    REQUEST_TIMEOUT(408, "请求超时"),//请求超时
    INTERNAL_SERVER_ERROR(500, "系统内部错误"),//服务器出错
    BAD_GATEWAY(502, "无效的请求"),//无效的请求
    SERVICE_UNAVAILABLE(503, "服务器出错"),//服务器不可用
    GATEWAY_TIMEOUT(504, "网络超时"),//网关响应超时
    UNKNOWN_ERROR(1000, "未知错误"),
    PARSE_ERROR(4000, "解析错误"),
    SSL_ERROR(5000, "证书错误")
}

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
            return if (throwable is HttpException) {
                when (throwable.code()) {
                    ErrorMsg.UNAUTHORIZED.code -> ErrorMsg.UNAUTHORIZED
                    ErrorMsg.FORBIDDEN.code -> ErrorMsg.FORBIDDEN
                    ErrorMsg.NOT_FOUND.code -> ErrorMsg.NOT_FOUND
                    ErrorMsg.REQUEST_TIMEOUT.code -> ErrorMsg.REQUEST_TIMEOUT
                    ErrorMsg.GATEWAY_TIMEOUT.code -> ErrorMsg.GATEWAY_TIMEOUT
                    ErrorMsg.INTERNAL_SERVER_ERROR.code -> ErrorMsg.INTERNAL_SERVER_ERROR
                    ErrorMsg.BAD_GATEWAY.code -> ErrorMsg.BAD_GATEWAY
                    ErrorMsg.SERVICE_UNAVAILABLE.code -> ErrorMsg.BAD_GATEWAY
                    else -> {
                        ErrorMsg.UNKNOWN_ERROR
                    }
                }
            } else if (throwable is JSONException || throwable is ParseException) {
                ErrorMsg.PARSE_ERROR
            } else if (throwable is ConnectException) {
                ErrorMsg.GATEWAY_TIMEOUT
            } else if (throwable is SSLPeerUnverifiedException || throwable is SSLHandshakeException) {
                ErrorMsg.SSL_ERROR
            } else if (throwable is SocketTimeoutException) {
                ErrorMsg.REQUEST_TIMEOUT
            } else if (throwable is ClassCastException) {
                ErrorMsg.PARSE_ERROR
            } else if (throwable is NullPointerException) {
                ErrorMsg.PARSE_ERROR
            } else if (throwable is FormatException) {
                ErrorMsg.PARSE_ERROR
            } else if (throwable is UnknownHostException) {
                ErrorMsg.NOT_FOUND
            } else {
                ErrorMsg.UNKNOWN_ERROR
            }
        }
    }
}