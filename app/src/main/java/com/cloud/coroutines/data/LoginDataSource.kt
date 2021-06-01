package com.cloud.coroutines.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.cloud.coroutines.data.model.User
import com.cloud.coroutines.network.ApiService
import com.cloud.coroutines.network.ErrorMsgFactory
import com.cloud.coroutines.network.safeApiCall

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(
    private val service: ApiService,
    private val preferences: SharedPreferences
) {

    suspend fun login(username: String, password: String) = safeApiCall {
        service.login(username = username, password = password)
    }

    suspend fun logout() = safeApiCall {
        service.logout()
    }

    suspend fun loginTest(username: String, password: String): Result<User> {
        return try {
            val response = service.login(username = username, password = password)
            if (response.successful) {
                Result.Success(response.data)
            } else {
                Result.Error(response.errorCode, response.errorMsg)
            }
        } catch (e: Throwable) {
            val errorMsg = ErrorMsgFactory.create(e)
            Result.Error(errorMsg.code, errorMsg.message)
        }
    }

    fun saveUser(user: User) {
        preferences.edit {
            putInt(UserContract.ID, user.id)
            putString(UserContract.EMAIL, user.email)
            putString(UserContract.NICK_NAME, user.nickname)
            putString(UserContract.USER_NAME, user.username)
        }
    }

    fun loadUser(): User? {
        if (preferences.all.isEmpty()) return null
        return User().apply {
            id = preferences.getInt(UserContract.ID, 0)
            email = preferences.getString(UserContract.EMAIL, "").toString()
            nickname = preferences.getString(UserContract.NICK_NAME, "").toString()
            username = preferences.getString(UserContract.USER_NAME, "").toString()
        }
    }

    object UserContract {
        const val ID = "id"
        const val EMAIL = "email"
        const val NICK_NAME = "nickname"
        const val USER_NAME = "username"
    }
}