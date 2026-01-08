package com.example.vidiary.data.domain.usecase

import com.example.vidiary.domain.repository.VideoRepository
import com.example.vidiary.domain.usecase.DeleteVideoUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteVideoUseCaseTest {

    private val repository = mockk<VideoRepository>(relaxed = true)
    private val deleteVideoUseCase = DeleteVideoUseCase(repository)

    @Test
    fun `invoke should call deleteVideo on repository`() = runTest {
        val videoId = 1L

        deleteVideoUseCase(videoId)

        coVerify { repository.deleteVideo(videoId) }
    }
}
