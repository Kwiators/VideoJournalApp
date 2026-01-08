package com.example.vidiary.ui.presentation.common

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    uri: Uri,
    modifier: Modifier = Modifier,
    playWhenReady: Boolean = true,
    onVideoEnded: () -> Unit = {}
) {
    val context = LocalContext.current

    val currentOnVideoEnded by rememberUpdatedState(onVideoEnded)

    val exoPlayer =
        remember(uri) {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
                this.playWhenReady = playWhenReady
            }
        }

    // Update playWhenReady when it changes and restart if ended
    LaunchedEffect(playWhenReady) {
        if (playWhenReady && exoPlayer.playbackState == Player.STATE_ENDED) {
            exoPlayer.seekTo(0)
        }
        exoPlayer.playWhenReady = playWhenReady
    }

    DisposableEffect(exoPlayer) {
        val listener =
            object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        currentOnVideoEnded()
                    }
                }
            }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false // Hide controller for TikTok-like experience
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
            }
        },
        update = {
            it.player = exoPlayer
            it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        },
        modifier = modifier
    )
}
