package com.cloud.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cloud.coroutines.network.ApiService
import com.cloud.coroutines.ui.login.LoginViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

            val service = ApiService.create()
            return LoginViewModel(
                loginRepository = Injection.provideDataRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}