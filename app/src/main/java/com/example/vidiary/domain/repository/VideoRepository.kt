package com.example.vidiary.domain.repository

import com.example.vidiary.domain.model.VideoClip
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun getVideos(): Flow<List<VideoClip>>
    suspend fun saveVideo(filePath: String, description: String?)
    suspend fun deleteVideo(id: Long)
}
