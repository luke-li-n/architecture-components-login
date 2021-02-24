package com.cloud.coroutines.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.cloud.coroutines.data.model.LoggedInUser
import com.cloud.coroutines.network.ApiResult
import com.cloud.coroutines.network.ApiService

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(
    private val service: ApiService,
    private val preferences: SharedPreferences
) {

    suspend fun login(username: String, password: String): ApiResult<LoggedInUser> {
        return try {
            val response = service.login(username = username, password = password)
            ApiResult.success(response)
        } catch (throwable: Throwable) {
            ApiResult.failure(throwable)
        }
    }

    suspend fun loginTest(username: String, password: String): Result<LoggedInUser> {
        return try {
            val response = service.login(username = username, password = password)
            if (response.succeeded) {
                Result.Success(response.data)
            } else {
                Result.Error(response.errorMsg)
            }
        } catch (e: Throwable) {
            Result.Error("Network request Cause Exception!")
        }
    }

    fun saveUser(user: LoggedInUser) {
        preferences.edit {
            putInt(UserContract.ID, user.id)
            putString(UserContract.EMAIL, user.email)
            putString(UserContract.NICK_NAME, user.nickname)
            putString(UserContract.USER_NAME, user.username)
        }
    }

    fun loadUser(): LoggedInUser? {
        if (preferences.all.isEmpty()) return null
        val user = LoggedInUser()
        user.id = preferences.getInt(UserContract.ID, 0)
        user.email = preferences.getString(UserContract.EMAIL, "").toString()
        user.nickname = preferences.getString(UserContract.NICK_NAME, "").toString()
        user.username = preferences.getString(UserContract.USER_NAME, "").toString()
        return user
    }

    suspend fun logout() {
        try {
            service.logout()
        } catch (e: Throwable) {
            Result.Error("Network request Cause Exception!")
        }
    }

    object UserContract {
        const val ID = "id"
        const val EMAIL = "email"
        const val NICK_NAME = "nickname"
        const val USER_NAME = "username"
    }
}