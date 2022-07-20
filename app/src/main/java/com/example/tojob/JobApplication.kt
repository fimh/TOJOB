package com.example.tojob

import android.app.Application

class JobApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Locator.initWith(this)
    }
}
