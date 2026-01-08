package com.example.vidiary.ui.presentation.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.vidiary.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onVideoSaved: () -> Unit,
    onBack: () -> Unit,
    viewModel: CameraViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionsState =
        rememberMultiplePermissionsState(
            permissions =
                listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
        )

    val showRationale = permissionsState.shouldShowRationale

    var isRecording by remember { mutableStateOf(false) }
    var recording: Recording? by remember { mutableStateOf(null) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var recordedFile by remember { mutableStateOf<File?>(null) }
    var description by remember { mutableStateOf("") }

    val toastRecordingFail =
        stringResource(R.string.camera_screen_recording_fail_toast_message)

    val toastAudioPermissionFail =
        stringResource(R.string.camera_screen_audio_permission_fail_toast_message)

    val toastAudioPermissionRequired =
        stringResource(R.string.camera_screen_audio_permission_required_toast_message)

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.VIDEO_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(Unit) { permissionsState.launchMultiplePermissionRequest() }

    if (permissionsState.allPermissionsGranted) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CameraPreview(
                controller = cameraController,
                lifecycleOwner = lifecycleOwner,
                modifier = Modifier.fillMaxSize()
            )

            // Controls Overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row for Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (!isRecording) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.background(
                                MaterialTheme.colorScheme.surface.copy(
                                    alpha = 0.5f
                                ),
                                CircleShape
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.camera_screen_close_button_label),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isRecording) {
                        // Empty space to balance the layout
                        Spacer(modifier = Modifier.size(48.dp))
                    }

                    // Record Button
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.Transparent, CircleShape)
                            .border(
                                4.dp,
                                MaterialTheme.colorScheme.onSurface,
                                CircleShape
                            )
                            .clickable {
                                if (isRecording) {
                                    recording?.stop()
                                    recording = null
                                    isRecording = false
                                } else {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.RECORD_AUDIO
                                        ) ==
                                        android.content.pm
                                            .PackageManager
                                            .PERMISSION_GRANTED
                                    ) {
                                        val newFile =
                                            File(
                                                context.filesDir,
                                                "video_${System.currentTimeMillis()}.mp4"
                                            )
                                        recordedFile = newFile

                                        val outputOptions =
                                            FileOutputOptions.Builder(newFile)
                                                .build()
                                        try {
                                            recording =
                                                cameraController.startRecording(
                                                    outputOptions,
                                                    AudioConfig.create(
                                                        true
                                                    ),
                                                    ContextCompat
                                                        .getMainExecutor(
                                                            context
                                                        )
                                                ) { event ->
                                                    when (event) {
                                                        is VideoRecordEvent.Finalize -> {
                                                            if (event.hasError()
                                                            ) {
                                                                recording
                                                                    ?.close()
                                                                recording = null
                                                                Toast.makeText(
                                                                    context,
                                                                    (toastRecordingFail + (event.cause?.message
                                                                        ?: "n/a")),
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                    .show()
                                                            } else {
                                                                showSaveDialog =
                                                                    true
                                                            }
                                                        }
                                                    }
                                                }
                                            isRecording = true
                                        } catch (_: SecurityException) {
                                            Toast.makeText(
                                                context,
                                                toastAudioPermissionFail,
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            toastAudioPermissionRequired,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isRecording) 30.dp else 60.dp)
                                .background(
                                    MaterialTheme.colorScheme.error,
                                    CircleShape
                                )
                        )
                    }

                    if (!isRecording) {
                        // Camera Switch Button
                        IconButton(
                            onClick = {
                                cameraController.cameraSelector =
                                    if (cameraController.cameraSelector ==
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    ) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface.copy(
                                        alpha = 0.5f
                                    ),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraAndroid,
                                contentDescription = stringResource(R.string.camera_screen_switch_camera_button_label),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        if (showSaveDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(
                        stringResource(R.string.camera_screen_save_video_alert_title)
                    )
                },
                text = {
                    Column {
                        Text(
                            stringResource(R.string.camera_screen_save_video_alert_text_field_label)
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            recordedFile?.let { file ->
                                viewModel.saveVideo(
                                    file,
                                    description.takeIf { it.isNotBlank() }
                                )
                                onVideoSaved()
                            }
                        }
                    ) {
                        Text(
                            stringResource(R.string.camera_screen_save_video_alert_save_button_label)
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            recordedFile?.delete()
                            showSaveDialog = false
                        }
                    ) {
                        Text(
                            stringResource(R.string.camera_screen_save_video_alert_discard_button_label)
                        )
                    }
                }
            )
        }
    } else {
        PermissionDeniedContent(
            showRationale = showRationale,
            onRequestPermission = { permissionsState.launchMultiplePermissionRequest() },
            onOpenSettings = {
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                context.startActivity(intent)
            },
            onBack = onBack
        )
    }
}

@Composable
fun PermissionDeniedContent(
    showRationale: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Visual indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PermissionIcon(
                    icon = Icons.Default.Videocam,
                    stringResource(R.string.camera_screen_no_permission_camera_icon_label)
                )
                PermissionIcon(
                    icon = Icons.Default.Mic,
                    stringResource(R.string.camera_screen_no_permission_microphone_icon_label)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.camera_screen_no_permission_header),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text =
                    if (showRationale) {
                        stringResource(R.string.camera_screen_no_permission_subheading)
                    } else {
                        stringResource(R.string.camera_screen_no_permission_permanently_denied_subheading)
                    },
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            if (showRationale) {
                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CircleShape
                ) {
                    Text(
                        stringResource(R.string.camera_screen_no_permission_grant_permissions_button_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                Button(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CircleShape
                ) {
                    Text(
                        stringResource(R.string.camera_screen_no_permission_open_settings_button_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.camera_screen_no_permission_back_button_label),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun PermissionIcon(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.1f
                    ), CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}
