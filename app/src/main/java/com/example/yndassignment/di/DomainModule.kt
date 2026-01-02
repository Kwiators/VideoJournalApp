package com.example.yndassignment.di

import com.example.yndassignment.data.domain.usecase.GetVideosUseCase
import com.example.yndassignment.data.domain.usecase.SaveVideoUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetVideosUseCase(get()) }
    factory { SaveVideoUseCase(get()) }
}
