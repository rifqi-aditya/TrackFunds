package com.rifqi.trackfunds.feature.scan.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.core.content.ContextCompat
import com.rifqi.trackfunds.feature.scan.ui.components.CameraPreview
import java.io.File
import java.util.concurrent.Executor

@Composable
fun CameraScanScreen(
    onNavigateBack: () -> Unit,
    onPhotoTaken: (Uri) -> Unit
) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Launcher untuk meminta izin kamera
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    // Minta izin saat pertama kali layar dibuka
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            // Jika izin diberikan, tampilkan UI Kamera
            val imageCapture = remember { ImageCapture.Builder().build() }

            CameraPreview(imageCapture = imageCapture, modifier = Modifier.fillMaxSize())

            // Tombol Shutter untuk mengambil foto
            FloatingActionButton(
                onClick = {
                    takePhoto(
                        context = context,
                        imageCapture = imageCapture,
                        onPhotoTaken = onPhotoTaken // Teruskan callback ke fungsi helper
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
            ) {
                Icon(Icons.Default.Camera, contentDescription = "Take Photo")
            }
        } else {
            // Tampilan jika izin ditolak
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Camera permission is required to use this feature.",
                    textAlign = TextAlign.Center
                )
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Grant Permission")
                }
            }
        }

        // Tombol kembali di pojok atas
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
        }
    }
}

// Fungsi helper untuk mengambil dan menyimpan foto menggunakan CameraX
private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onPhotoTaken: (Uri) -> Unit,
    executor: Executor = ContextCompat.getMainExecutor(context)
) {
    val photoFile = File(
        context.cacheDir,
        "receipt_${System.currentTimeMillis()}.jpg"
    )
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                onPhotoTaken(savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                println("Error taking photo: ${exception.message}")
            }
        }
    )
}