package com.example.yndassignment.presentation.feed

import com.example.yndassignment.MainDispatcherRule
import com.example.yndassignment.data.domain.model.VideoClip
import com.example.yndassignment.data.domain.usecase.GetVideosUseCase
import com.example.yndassignment.ui.presentation.feed.FeedViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FeedViewModelTest {

    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val getVideosUseCase = mockk<GetVideosUseCase>()

    @Test
    fun `videos state updates when use case emits videos`() {
        // Given
        val mockVideos =
                listOf(
                        VideoClip(1, "path/1", "desc1", 1000L),
                        VideoClip(2, "path/2", "desc2", 2000L)
                )
        every { getVideosUseCase() } returns flowOf(mockVideos)

        val viewModel = FeedViewModel(getVideosUseCase)

        // Then
        assertEquals(mockVideos, viewModel.videos.value)
    }
}
