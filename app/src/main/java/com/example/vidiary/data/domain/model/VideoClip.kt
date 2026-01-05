package com.example.vidiary.data.domain.model

data class VideoClip(
    val id: Long = 0,
    val filePath: String,
    val description: String?,
    val timestamp: Long
)