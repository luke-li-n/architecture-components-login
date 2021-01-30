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
        return applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}