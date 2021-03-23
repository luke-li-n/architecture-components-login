package com.cloud.coroutines.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cloud.coroutines.R
import com.cloud.coroutines.data.LoginRepository
import com.cloud.coroutines.data.error
import com.cloud.coroutines.data.success

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    fun login(username: String, password: String) = liveData {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)
        Log.e("LoginViewModel", Thread.currentThread().name)
        result.success {
            val loginResult = LoginResult(success = LoggedInUserView(displayName = it?.username.toString()))
            emit(loginResult)
        }.error {
            emit(LoginResult(error = it))
        }
    }

    fun loginDataChanged(username: String, password: String) = liveData {
        if (!isUserNameValid(username)) {
            val uiState = LoginFormState(usernameError = R.string.invalid_username)
            emit(uiState)
        } else if (!isPasswordValid(password)) {
            val uiState = LoginFormState(passwordError = R.string.invalid_password)
            emit(uiState)
        } else {
            val uiState = LoginFormState(isDataValid = true)
            emit(uiState)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}