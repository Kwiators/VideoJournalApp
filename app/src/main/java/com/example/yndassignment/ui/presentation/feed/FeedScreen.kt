package com.example.yndassignment.ui.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.yndassignment.data.domain.model.VideoClip
import com.example.yndassignment.ui.presentation.common.VideoPlayer
import java.io.File
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun FeedScreen(onNavigateToCamera: () -> Unit, viewModel: FeedViewModel = koinViewModel()) {
    val videos by viewModel.videos.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCamera) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Record Video")
            }
        }
    ) { padding ->
        if (videos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(text = "No videos yet. Tap + to record!") }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) { items(videos, key = { it.id }) { video -> VideoItem(video) } }
        }
    }
}

@Composable
fun VideoItem(video: VideoClip) {
    var isPlaying by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            if (isPlaying) {
                VideoPlayer(uri = File(video.filePath).toUri())
            } else {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(Color.Black)
                            .clickable { isPlaying = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .height(64.dp)
                            .fillMaxWidth()
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                if (!video.description.isNullOrBlank()) {
                    Text(text = video.description, style = MaterialTheme.typography.bodyLarge)
                }
                Text(
                    text =
                        " recorded on ${
                            SimpleDateFormat("dd/MM/yyyy HH:mm")
                                .format(Date(video.timestamp))
                        }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
