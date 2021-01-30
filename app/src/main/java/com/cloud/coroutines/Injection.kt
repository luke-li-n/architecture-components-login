package com.cloud.coroutines

import android.content.SharedPreferences
import com.cloud.coroutines.data.LoginDataSource
import com.cloud.coroutines.data.LoginRepository
import com.cloud.coroutines.network.ApiService

object Injection {
    fun provideDataRepository(): LoginRepository {
        return LoginRepository(dataSource = provideDataSource())
    }

    private fun provideDataSource(): LoginDataSource {
        val service = provideApiService()
        val preferences = provideSharedPreferences()
        return LoginDataSource(service, preferences)
    }

    private fun provideApiService(): ApiService {
        return ApiService.create()
    }

    private fun provideSharedPreferences(): SharedPreferences {
        return App.instance.getUserPreferences()
    }
}