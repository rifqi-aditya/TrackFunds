package com.rifqi.trackfunds.feature.budget.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 10.dp
) {
    val animatedProgress by animateFloatAsState(progress)

    // Logika untuk menentukan warna berdasarkan progres
    val progressColor = when {
        progress >= 0.8f -> TrackFundsTheme.extendedColors.chartConditionWarning
        progress > 0.6f -> TrackFundsTheme.extendedColors.chartConditionGood
        else -> TrackFundsTheme.extendedColors.accentGreen
    }

    val trackColor = TrackFundsTheme.extendedColors.accentGreen.copy(alpha = 0.1f)

    // FIX 2: Lakukan konversi Dp ke Px di sini, di dalam lingkup Composable
    val strokePx = with(LocalDensity.current) { strokeWidth.toPx() }

    Canvas(modifier = modifier) {
        // FIX 3: Gunakan nilai Px yang sudah dikonversi
        val strokeStyle = Stroke(width = strokePx, cap = StrokeCap.Round)

        // Track (latar belakang)
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = strokeStyle
        )

        // Progress
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = strokeStyle
        )
    }
}