package com.rifqi.trackfunds.feature.scan.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rifqi.trackfunds.feature.scan.ui.event.CameraEvent
import com.rifqi.trackfunds.feature.scan.ui.sideeffect.CameraSideEffect
import com.rifqi.trackfunds.feature.scan.ui.viewmodel.CameraViewModel
import java.io.File


// --- Stateful Composable ---
@Composable
fun CameraScanScreen(
    onNavigateBack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                CameraSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    val context = LocalContext.current
    val cameraController = remember { LifecycleCameraController(context) }

    CameraScanContent(
        hasPermission = hasCameraPermission,
        controller = cameraController,
        onNavigateBack = onNavigateBack,
        onTakePhoto = {
            takePhoto(
                context = context,
                controller = cameraController,
                onPhotoTaken = { uri ->
                    viewModel.onEvent(CameraEvent.PhotoTaken(uri))
                }
            )
        },
        onGrantPermissionClick = {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    )
}

// --- Stateless Composable ---
@Composable
private fun CameraScanContent(
    hasPermission: Boolean,
    controller: LifecycleCameraController, // 2. Controller diterima sebagai parameter
    onNavigateBack: () -> Unit,
    onTakePhoto: () -> Unit,
    onGrantPermissionClick: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasPermission) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        this.controller = controller
                        controller.bindToLifecycle(lifecycleOwner)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = onTakePhoto,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Take Photo",
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }
        } else {
            // Tampilan jika izin ditolak
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Camera permission is required to scan receipts.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = onGrantPermissionClick) {
                    Text("Grant Permission")
                }
            }
        }

        // Tombol Kembali
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                "Back",
                tint = Color.White,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    .padding(8.dp)
            )
        }
    }
}

// Fungsi helper yang lebih sederhana
private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Uri) -> Unit
) {
    val file = File.createTempFile("receipt_", ".jpg", context.cacheDir)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                output.savedUri?.let(onPhotoTaken)
            }

            override fun onError(exception: ImageCaptureException) {
                // Handle error, misalnya dengan menampilkan Snackbar
                println("Error taking photo: ${exception.message}")
            }
        }
    )
}