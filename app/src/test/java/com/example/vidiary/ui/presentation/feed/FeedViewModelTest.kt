package com.example.vidiary.ui.presentation.feed

import com.example.vidiary.MainDispatcherRule
import com.example.vidiary.data.domain.model.VideoClip
import com.example.vidiary.data.domain.usecase.DeleteVideoUseCase
import com.example.vidiary.data.domain.usecase.GetVideosUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FeedViewModelTest {

    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val getVideosUseCase = mockk<GetVideosUseCase>()
    private val deleteVideoUseCase = mockk<DeleteVideoUseCase>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `videos state updates when use case emits videos`() = runTest {
        // Given
        val mockVideos =
                listOf(
                        VideoClip(1, "path/1", "desc1", 1000L),
                        VideoClip(2, "path/2", "desc2", 2000L)
                )
        every { getVideosUseCase() } returns flowOf(mockVideos)

        val viewModel = FeedViewModel(getVideosUseCase, deleteVideoUseCase)

        // To make StateFlow with SharingStarted.WhileSubscribed collect, we need a subscriber
        val job = launch(UnconfinedTestDispatcher()) { viewModel.videos.collect {} }

        // Then
        assertEquals(mockVideos, viewModel.videos.value)
        job.cancel()
    }

    @Test
    fun `deleteVideo should call deleteVideoUseCase`() = runTest {
        // Given
        val video = VideoClip(1, "path/1", "desc1", 1000L)
        every { getVideosUseCase() } returns flowOf(emptyList())
        val viewModel = FeedViewModel(getVideosUseCase, deleteVideoUseCase)

        // When
        viewModel.deleteVideo(video)

        // Then
        coVerify { deleteVideoUseCase(video.id) }
    }
}
