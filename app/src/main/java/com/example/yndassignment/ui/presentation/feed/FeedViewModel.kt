package com.example.yndassignment.ui.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yndassignment.data.domain.model.VideoClip
import com.example.yndassignment.data.domain.usecase.GetVideosUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FeedViewModel(getVideosUseCase: GetVideosUseCase) : ViewModel() {

    val videos: StateFlow<List<VideoClip>> =
            getVideosUseCase()
                    .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = emptyList()
                    )
}
