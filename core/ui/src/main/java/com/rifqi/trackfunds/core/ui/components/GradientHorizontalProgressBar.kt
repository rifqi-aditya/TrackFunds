package com.rifqi.trackfunds.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun GradientHorizontalProgressBar(
    progress: Float,
    brush: Brush,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    // State untuk animasi tidak berubah
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) progress.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "ProgressBarAnimation"
    )
    LaunchedEffect(Unit) { animationPlayed = true }

    // Alat untuk mengukur ukuran teks agar bisa diposisikan di tengah
    val textMeasurer = rememberTextMeasurer()
    val percentageText = "${(progress * 100).toInt()}%"
    val textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.White)

    // FIX: Gunakan Canvas untuk menggambar semuanya, ini memberi kita kontrol penuh
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // Gambar 1: Latar belakang (track)
        drawRect(color = trackColor)

        // Gambar 2: Progress bar yang terisi
        drawRect(
            brush = brush,
            size = Size(width = size.width * animatedProgress, height = size.height)
        )

        // Gambar 3: Teks persentase di tengah
        val textSize = textMeasurer.measure(text = percentageText, style = textStyle)
        val textPosition = Offset(
            x = (size.width - textSize.size.width) / 2,
            y = (size.height - textSize.size.height) / 2
        )
        drawText(
            textMeasurer = textMeasurer,
            text = percentageText,
            style = textStyle,
            topLeft = textPosition
        )
    }
}