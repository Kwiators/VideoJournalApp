package com.example.yndassignment.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.yndassignment.data.repository.VideoRepositoryImpl
import com.example.yndassignment.data.source.local.VideoDatabase
import com.example.yndassignment.data.domain.repository.VideoRepository
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
