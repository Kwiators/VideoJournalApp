package com.example.yndassignment.ui.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yndassignment.data.domain.usecase.SaveVideoUseCase
import java.io.File
import kotlinx.coroutines.launch

class CameraViewModel(private val saveVideoUseCase: SaveVideoUseCase) : ViewModel() {

    fun saveVideo(videoFile: File, description: String?) {
        viewModelScope.launch { saveVideoUseCase(videoFile.absolutePath, description) }
    }
}
