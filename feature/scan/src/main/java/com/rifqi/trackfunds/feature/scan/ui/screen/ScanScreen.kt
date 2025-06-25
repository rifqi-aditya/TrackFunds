package com.rifqi.trackfunds.feature.scan.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.rifqi.trackfunds.feature.scan.ui.components.CameraPreview
import com.rifqi.trackfunds.feature.scan.ui.event.ScanEvent
import com.rifqi.trackfunds.feature.scan.ui.viewmodel.ScanViewModel
import java.io.File

@Composable
fun ScanScreen(
    viewModel: ScanViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var hasCamPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCamPermission = granted }
    )
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(uiState.isScanSuccessful) {
        if (uiState.isScanSuccessful) {
            onNavigateBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCamPermission) {
            val imageCapture = remember { ImageCapture.Builder().build() }

            if (uiState.capturedImageUri == null) {
                // Tampilkan pratinjau kamera jika belum ada foto
                CameraPreview(imageCapture = imageCapture)
                Button(
                    onClick = {
                        takePhoto(
                            context = context,
                            imageCapture = imageCapture,
                            onPhotoTaken = { uri -> viewModel.onEvent(ScanEvent.PhotoTaken(uri)) }
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(32.dp)
                ) {
                    Icon(Icons.Default.Camera, "Take Photo")
                }
            } else {
                // Tampilkan foto yang sudah diambil untuk konfirmasi
                Image(
                    painter = rememberAsyncImagePainter(uiState.capturedImageUri),
                    contentDescription = "Captured Receipt",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(32.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = { viewModel.onEvent(ScanEvent.RetakePhoto) }) { Text("Retake") }
                    Button(onClick = {
                        // --- TAMBAHKAN PRINTLN INI ---
                        println("✅ 1. Tombol 'Use This Photo' Ditekan")
                        viewModel.onEvent(ScanEvent.ConfirmPhoto(context))
                    }) { Text("Use This Photo") }
                }
            }
        } else {
            // Tampilan jika izin ditolak
            Text(
                "Camera permission is needed to scan receipts.",
                modifier = Modifier.align(Alignment.Center)
            )
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

        // Tampilkan loading overlay
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

// Fungsi helper untuk mengambil dan menyimpan foto
private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onPhotoTaken: (Uri) -> Unit
) {
    val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoTaken(outputFileResults.savedUri ?: Uri.fromFile(file))
            }

            override fun onError(exception: ImageCaptureException) {
                // Handle error
            }
        }
    )
}