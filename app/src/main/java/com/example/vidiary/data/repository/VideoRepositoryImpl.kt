package com.example.vidiary.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.vidiary.data.domain.model.VideoClip
import com.example.vidiary.data.domain.repository.VideoRepository
import com.example.vidiary.data.source.local.VideoDatabase
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

    override suspend fun deleteVideo(id: Long) {
        withContext(Dispatchers.IO) {
            db.videoDatabaseQueries.deleteVideo(id)
        }
    }
}
