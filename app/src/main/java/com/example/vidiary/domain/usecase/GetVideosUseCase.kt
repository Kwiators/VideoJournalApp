package com.example.vidiary.domain.usecase

import com.example.vidiary.domain.model.VideoClip
import com.example.vidiary.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow

class GetVideosUseCase(private val repository: VideoRepository) {
    operator fun invoke(): Flow<List<VideoClip>> {
        return repository.getVideos()
    }
}
