package com.example.vidiary.data.domain.usecase

import com.example.vidiary.data.domain.repository.VideoRepository

class SaveVideoUseCase(private val repository: VideoRepository) {
    suspend operator fun invoke(filePath: String, description: String?) {
        repository.saveVideo(filePath, description)
    }
}
