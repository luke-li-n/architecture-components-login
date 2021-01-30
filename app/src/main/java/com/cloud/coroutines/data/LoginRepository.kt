package com.cloud.coroutines.data

import com.cloud.coroutines.data.model.LoggedInUser
import com.cloud.coroutines.network.ApiResult

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = dataSource.loadUser()
    }

    suspend fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)
        return when (result) {
            is ApiResult.SuccessResult -> {
                setLoggedInUser(result.data)
                Result.Success(result.data)
            }
            is ApiResult.ErrorResult -> {
                Result.Error(result.errorMsg)
            }
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser?) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        loggedInUser?.let { dataSource.saveUser(it) }
    }
}