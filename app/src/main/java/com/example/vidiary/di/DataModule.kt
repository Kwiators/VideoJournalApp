package com.example.vidiary.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.vidiary.data.repository.VideoRepositoryImpl
import com.example.vidiary.data.source.local.VideoDatabase
import com.example.vidiary.domain.repository.VideoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
                schema = VideoDatabase.Schema,
                context = androidContext(),
                name = "video.db"
        )
    }

    single { VideoDatabase(get()) }

    single<VideoRepository> { VideoRepositoryImpl(get()) }
}
