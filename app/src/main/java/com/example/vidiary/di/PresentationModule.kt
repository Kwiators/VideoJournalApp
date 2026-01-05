package com.example.vidiary.di

import com.example.vidiary.ui.presentation.camera.CameraViewModel
import com.example.vidiary.ui.presentation.feed.FeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::FeedViewModel)
    viewModelOf(::CameraViewModel)
}
