package com.example.composesampleapplication20230220

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application() {
    companion object {
        private var instance: TodoApplication? = null
        fun getInstance(): TodoApplication {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}