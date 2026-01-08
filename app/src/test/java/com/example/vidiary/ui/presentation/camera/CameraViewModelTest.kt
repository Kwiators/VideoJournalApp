package com.example.vidiary.ui.presentation.camera

import com.example.vidiary.MainDispatcherRule
import com.example.vidiary.data.domain.usecase.SaveVideoUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.File

class CameraViewModelTest {

    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val saveVideoUseCase = mockk<SaveVideoUseCase>(relaxed = true)
    private val viewModel = CameraViewModel(saveVideoUseCase)

    @Test
    fun `saveVideo should call saveVideoUseCase with correct parameters`() = runTest {
        // Given
        val file = File("test.mp4")
        val description = "Test Description"

        // When
        viewModel.saveVideo(file, description)

        // Then
        coVerify { saveVideoUseCase(file.absolutePath, description) }
    }
}
