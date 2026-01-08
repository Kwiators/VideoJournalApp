package com.example.vidiary.domain.usecase

import com.example.vidiary.domain.repository.VideoRepository

class DeleteVideoUseCase(private val repository: VideoRepository) {
    suspend operator fun invoke(id: Long) {
        repository.deleteVideo(id)
    }
}
