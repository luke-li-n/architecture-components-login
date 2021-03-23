package com.cloud.coroutines.data

import com.cloud.coroutines.data.model.User

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = dataSource.loadUser()
    }

    suspend fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<User> {
        val result = dataSource.login(username, password)
        result.success {
            setLoggedInUser(it)
        }
        return result
    }

    private fun setLoggedInUser(user: User?) {
        this.user = user
        user?.let { dataSource.saveUser(it) }
    }
}