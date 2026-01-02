package com.example.yndassignment.data.domain.usecase

import com.example.yndassignment.data.domain.model.VideoClip
import com.example.yndassignment.data.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow

class GetVideosUseCase(private val repository: VideoRepository) {
    operator fun invoke(): Flow<List<VideoClip>> {
        return repository.getVideos()
    }
}
