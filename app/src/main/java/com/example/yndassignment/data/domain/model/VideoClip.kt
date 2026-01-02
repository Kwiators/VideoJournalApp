package com.example.yndassignment.data.domain.model

data class VideoClip(
    val id: Long = 0,
    val filePath: String,
    val description: String?,
    val timestamp: Long
)