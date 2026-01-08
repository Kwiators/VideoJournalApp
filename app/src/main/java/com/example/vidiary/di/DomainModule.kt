package com.example.vidiary.di

import com.example.vidiary.domain.usecase.DeleteVideoUseCase
import com.example.vidiary.domain.usecase.GetVideosUseCase
import com.example.vidiary.domain.usecase.SaveVideoUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetVideosUseCase(get()) }
    factory { SaveVideoUseCase(get()) }
    factory { DeleteVideoUseCase(get()) }
}
