package com.example.vidiary.data.domain.usecase

import com.example.vidiary.domain.model.VideoClip
import com.example.vidiary.domain.repository.VideoRepository
import com.example.vidiary.domain.usecase.GetVideosUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetVideosUseCaseTest {

    private val repository = mockk<VideoRepository>()
    private val useCase = GetVideosUseCase(repository)

    @Test
    fun `invoke should return videos from repository`() = runTest {
        // Given
        val mockVideos =
                listOf(
                        VideoClip(1, "path/1", "desc1", 1000L),
                        VideoClip(2, "path/2", "desc2", 2000L)
                )
        every { repository.getVideos() } returns flowOf(mockVideos)

        // When
        val result = useCase().first()

        // Then
        assertEquals(mockVideos, result)
    }
}
