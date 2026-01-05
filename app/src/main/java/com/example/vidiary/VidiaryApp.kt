package com.example.vidiary

import android.app.Application
import com.example.vidiary.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VidiaryApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VidiaryApp)
            modules(appModule)
        }
    }
}
