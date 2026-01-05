package com.example.vidiary.data.domain.usecase

import com.example.vidiary.data.domain.model.VideoClip
import com.example.vidiary.data.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow

class GetVideosUseCase(private val repository: VideoRepository) {
    operator fun invoke(): Flow<List<VideoClip>> {
        return repository.getVideos()
    }
}
