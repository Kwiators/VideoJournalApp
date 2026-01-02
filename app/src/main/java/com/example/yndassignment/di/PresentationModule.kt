package com.example.yndassignment.di

import com.example.yndassignment.ui.presentation.camera.CameraViewModel
import com.example.yndassignment.ui.presentation.feed.FeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::FeedViewModel)
    viewModelOf(::CameraViewModel)
}
