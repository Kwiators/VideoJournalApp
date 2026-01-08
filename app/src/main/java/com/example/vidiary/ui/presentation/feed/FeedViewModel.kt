package com.example.vidiary.ui.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidiary.data.domain.model.VideoClip
import com.example.vidiary.data.domain.usecase.DeleteVideoUseCase
import com.example.vidiary.data.domain.usecase.GetVideosUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FeedViewModel(
    getVideosUseCase: GetVideosUseCase,
    private val deleteVideoUseCase: DeleteVideoUseCase
) : ViewModel() {

    val videos: StateFlow<List<VideoClip>> =
        getVideosUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun deleteVideo(video: VideoClip) {
        viewModelScope.launch { deleteVideoUseCase(video.id) }
    }
}
