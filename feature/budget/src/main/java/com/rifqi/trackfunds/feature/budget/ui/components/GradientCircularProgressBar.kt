package com.rifqi.trackfunds.feature.budget.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GradientCircularProgressBar(progress: Float, modifier: Modifier) {
    val animatedProgress by animateFloatAsState(progress)

    val gradient = Brush.sweepGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,     // Mulai dari Hijau Toska
            MaterialTheme.colorScheme.secondary,   // Berubah menjadi Biru
            MaterialTheme.colorScheme.tertiary,    // Berubah menjadi Ungu
            MaterialTheme.colorScheme.primary      // Kembali ke Hijau Toska untuk loop yang mulus
        )
    )

    Canvas(modifier = modifier) {
        val stroke = 10.dp.toPx()

        drawArc(
            color = Color.LightGray,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(stroke, cap = StrokeCap.Round)
        )

        drawArc(
            brush = gradient,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = Stroke(stroke, cap = StrokeCap.Round)
        )
    }
}