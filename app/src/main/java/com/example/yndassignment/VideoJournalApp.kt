package com.example.yndassignment

import android.app.Application
import com.example.yndassignment.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VideoJournalApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VideoJournalApp)
            modules(appModule)
        }
    }
}
