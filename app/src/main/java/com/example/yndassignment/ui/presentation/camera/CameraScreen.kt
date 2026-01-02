package com.example.yndassignment.ui.presentation.camera

import android.Manifest
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import org.koin.androidx.compose.koinViewModel


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
                listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        )

    var isRecording by remember { mutableStateOf(false) }
    var recording: Recording? by remember { mutableStateOf(null) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var recordedFile by remember { mutableStateOf<File?>(null) }
    var description by remember { mutableStateOf("") }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.VIDEO_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(Unit) { permissionsState.launchMultiplePermissionRequest() }

    if (permissionsState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )

            // Record Button
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .size(80.dp)
                        .background(Color.Transparent, CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .clickable
                        {
                            if (isRecording) {
                                recording?.stop()
                                recording = null
                                isRecording = false
                            } else {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.RECORD_AUDIO
                                    ) ==
                                    android.content.pm.PackageManager
                                        .PERMISSION_GRANTED
                                ) {
                                    val newFile =
                                        File(
                                            context.filesDir,
                                            "video_${System.currentTimeMillis()}.mp4"
                                        )
                                    recordedFile = newFile

                                    val outputOptions =
                                        FileOutputOptions.Builder(newFile).build()
                                    try {
                                        recording =
                                            cameraController.startRecording(
                                                outputOptions,
                                                AudioConfig.create(true),
                                                ContextCompat.getMainExecutor(
                                                    context
                                                )
                                            ) { event ->
                                                when (event) {
                                                    is VideoRecordEvent.Finalize -> {
                                                        if (event.hasError()) {
                                                            recording?.close()
                                                            recording = null
                                                            Toast.makeText(
                                                                context,
                                                                "Recording failed: ${event.cause?.message}",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                                .show()
                                                        } else {
                                                            showSaveDialog = true
                                                        }
                                                    }
                                                }
                                            }
                                        isRecording = true
                                    } catch (e: SecurityException) {
                                        Toast.makeText(
                                            context,
                                            "Permission required to record audio",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Audio permission is required",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(if (isRecording) 30.dp else 60.dp)
                            .background(Color.Red, CircleShape)
                )
            }
        }

        if (showSaveDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Save Video") },
                text = {
                    Column {
                        Text("Add a description (optional):")
                        Spacer(modifier = Modifier.height(8.dp))
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
                    ) { Text("Save") }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            // Discard video
                            recordedFile?.delete()
                            onVideoSaved() // Or onBack()
                        }
                    ) { Text("Discard") }
                }
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera and Audio permissions are required to record video.")
            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                Text("Grant Permissions")
            }
        }
    }
}
