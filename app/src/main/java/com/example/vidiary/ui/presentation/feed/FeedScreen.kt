package com.example.vidiary.ui.presentation.feed

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.vidiary.R
import com.example.vidiary.data.domain.model.VideoClip
import com.example.vidiary.ui.presentation.common.VideoPlayer
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FeedScreen(onNavigateToCamera: () -> Unit, viewModel: FeedViewModel = koinViewModel()) {
    val videos by viewModel.videos.collectAsState()

    if (videos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.feed_screen_videos_empty_banner),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onNavigateToCamera) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        stringResource(R.string.feed_screen_record_first_video)
                    )
                }
            }
        }
    } else {
        val pagerState = rememberPagerState(pageCount = { videos.size })

        Box(modifier = Modifier.fillMaxSize()) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                key = { videos[it].id }
            ) { pageIndex ->
                val video = videos[pageIndex]
                VideoItem(
                    video = video,
                    isFocused = pagerState.currentPage == pageIndex,
                    onDeleteClick = { viewModel.deleteVideo(video) }
                )
            }

            // Record Button
            Button(
                onClick = onNavigateToCamera,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                shape = CircleShape,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.primaryContainer.copy(
                                alpha = 0.9f
                            ),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    stringResource(R.string.feed_screen_record_video_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun VideoItem(video: VideoClip, isFocused: Boolean, onDeleteClick: () -> Unit) {
    var isPaused by remember { mutableStateOf(false) }
    var hasEnded by remember { mutableStateOf(false) }

    // Reset state when focus changes
    LaunchedEffect(isFocused) {
        if (isFocused) {
            isPaused = false
            hasEnded = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable {
                if (hasEnded) {
                    hasEnded = false
                    isPaused = false
                } else {
                    isPaused = !isPaused
                }
            }
    ) {
        // Video Player as background
        VideoPlayer(
            uri = File(video.filePath).toUri(),
            modifier = Modifier.fillMaxSize(),
            playWhenReady = isFocused && !isPaused && !hasEnded,
            onVideoEnded = { hasEnded = true }
        )

        // Replay Button Overlay
        if (hasEnded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = {
                            hasEnded = false
                            isPaused = false
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.feed_screen_replay_video_label),
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.feed_screen_replay_video_label),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // Pause Icon Overlay (Briefly show or show when paused)
        if (isPaused && !hasEnded) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Paused",
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
            )
        }

        // Bottom Overlay for Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                        startY = 0f
                    )
                )
                .padding(16.dp)
                .padding(bottom = 80.dp) // Space for centered Record button
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (!video.description.isNullOrBlank()) {
                        Text(
                            text = video.description,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(
                        text =
                            SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                                .format(Date(video.timestamp)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val context = LocalContext.current
                    var showDeleteDialog by remember { mutableStateOf(false) }

                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = {
                                Text(
                                    stringResource(R.string.feed_screen_delete_video_label),
                                )
                            },
                            text = {
                                Text(
                                    stringResource(R.string.feed_screen_delete_alert_content)
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showDeleteDialog = false
                                        onDeleteClick()
                                    }
                                ) {
                                    Text(
                                        stringResource(R.string.feed_screen_delete_alert_delete_button_label),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        showDeleteDialog = false
                                    }
                                ) {
                                    Text(
                                        stringResource(R.string.feed_screen_delete_alert_cancel_button_label)
                                    )
                                }
                            }
                        )
                    }

                    IconButton(
                        onClick = {
                            val file = File(video.filePath)
                            val uri =
                                FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                            val intent =
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "video/mp4"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                            context.startActivity(Intent.createChooser(intent, "Share Video"))
                        },
                        modifier =
                            Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.feed_screen_share_vide_button_label),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                            CircleShape
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.feed_screen_delete_video_button_label),
                            tint = MaterialTheme.colorScheme.onError
                        )
                    }
                }
            }
        }
    }
}
