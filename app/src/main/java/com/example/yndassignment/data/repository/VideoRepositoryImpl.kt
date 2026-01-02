package com.example.yndassignment.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.yndassignment.data.source.local.VideoDatabase
import com.example.yndassignment.data.domain.model.VideoClip
import com.example.yndassignment.data.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class VideoRepositoryImpl(
    private val db: VideoDatabase
) : VideoRepository {

    override fun getVideos(): Flow<List<VideoClip>> {
        return db.videoDatabaseQueries.getVideos()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    VideoClip(
                        id = entity.id,
                        filePath = entity.filePath,
                        description = entity.description,
                        timestamp = entity.timestamp
                    )
                }
            }
    }

    override suspend fun saveVideo(filePath: String, description: String?) {
        withContext(Dispatchers.IO) {
            db.videoDatabaseQueries.insertVideo(
                filePath = filePath,
                description = description,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}
