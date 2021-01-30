package com.cloud.coroutines.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.cloud.coroutines.data.LoginRepository
import com.cloud.coroutines.data.Result

import com.cloud.coroutines.R
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    fun login(username: String, password: String) = liveData {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)
        Log.e("LoginViewModel", Thread.currentThread().name)
        result.run {
            onSuccess {
                emit(LoginResult(success = LoggedInUserView(displayName = it?.username.toString())))
            }
            onError {
                emit(LoginResult(error = it))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) = liveData {
        if (!isUserNameValid(username)) {
            emit(LoginFormState(usernameError = R.string.invalid_username))
        } else if (!isPasswordValid(password)) {
            emit(LoginFormState(passwordError = R.string.invalid_password))
        } else {
            emit(LoginFormState(isDataValid = true))
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