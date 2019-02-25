package com.sotnik.geotracker

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ContextHelper.init(this)
    }
}