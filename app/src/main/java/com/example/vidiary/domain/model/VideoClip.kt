package com.example.vidiary.domain.model

data class VideoClip(
    val id: Long = 0,
    val filePath: String,
    val description: String?,
    val timestamp: Long
)