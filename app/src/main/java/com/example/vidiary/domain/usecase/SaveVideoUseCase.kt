package com.example.vidiary.domain.usecase

import com.example.vidiary.domain.repository.VideoRepository

class SaveVideoUseCase(private val repository: VideoRepository) {
    suspend operator fun invoke(filePath: String, description: String?) {
        repository.saveVideo(filePath, description)
    }
}
