package com.example.vidiary.data.domain.usecase

import com.example.vidiary.data.domain.repository.VideoRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SaveVideoUseCaseTest {

    private val repository = mockk<VideoRepository>(relaxed = true)
    private val useCase = SaveVideoUseCase(repository)

    @Test
    fun `invoke calls repository saveVideo`() = runTest {
        // Given
        val path = "path/to/video.mp4"
        val desc = "My Video"

        // When
        useCase(path, desc)

        // Then
        coVerify { repository.saveVideo(path, desc) }
    }
}
