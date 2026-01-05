package com.example.vidiary.data.domain.usecase

import com.example.vidiary.data.domain.repository.VideoRepository

class DeleteVideoUseCase(private val repository: VideoRepository) {
    suspend operator fun invoke(id: Long) {
        repository.deleteVideo(id)
    }
}
