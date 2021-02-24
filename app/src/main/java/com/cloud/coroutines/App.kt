package com.cloud.coroutines

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getUserPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREFERENCES_NAME = "user_profile"

        lateinit var instance: App
            private set
    }
}