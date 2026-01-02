package com.example.yndassignment.data.domain.usecase

import com.example.yndassignment.data.domain.repository.VideoRepository

class DeleteVideoUseCase(private val repository: VideoRepository) {
    suspend operator fun invoke(id: Long) {
        repository.deleteVideo(id)
    }
}
