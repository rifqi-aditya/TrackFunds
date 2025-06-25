package com.rifqi.trackfunds.feature.scan.ui.components

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    imageCapture: ImageCapture
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // AndroidView digunakan untuk menampung View dari sistem Android lama (PreviewView)
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = ContextCompat.getMainExecutor(ctx)

            // Coroutine untuk mendapatkan instance CameraProvider
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // Atur pratinjau
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                // Pilih kamera belakang
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                // Unbind semua use case sebelumnya sebelum me-rebind
                cameraProvider.unbindAll()

                // Bind use case ke lifecycle
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture // Gunakan instance ImageCapture dari luar
                )
            }, executor)
            previewView
        },
        modifier = modifier
    )
}