package com.example.yndassignment.di

import org.koin.dsl.module

val appModule = module {
    // Includes will be added here as we create other modules
    includes(dataModule, domainModule, presentationModule)
}
