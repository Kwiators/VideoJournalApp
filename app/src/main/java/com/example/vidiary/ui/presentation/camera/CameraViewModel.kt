package com.example.vidiary.ui.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidiary.data.domain.usecase.SaveVideoUseCase
import kotlinx.coroutines.launch
import java.io.File

class CameraViewModel(private val saveVideoUseCase: SaveVideoUseCase) : ViewModel() {

    fun saveVideo(videoFile: File, description: String?) {
        viewModelScope.launch { saveVideoUseCase(videoFile.absolutePath, description) }
    }
}
