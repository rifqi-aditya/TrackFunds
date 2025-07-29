package com.rifqi.trackfunds.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GradientLinearProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val progressBarBrush = remember(progress) {
        when {
            // Gradasi Oranye ke Merah
            progress > 0.8f -> Brush.horizontalGradient(
                colors = listOf(Color(0xFFFFA726), Color(0xFFD32F2F))
            )
            // Gradasi Kuning ke Oranye
            progress > 0.5f -> Brush.horizontalGradient(
                colors = listOf(Color(0xFFFFEE58), Color(0xFFFFA000))
            )
            // Gradasi Hijau Muda ke Hijau Tua
            else -> Brush.horizontalGradient(
                colors = listOf(Color(0xFF9CCC65), Color(0xFF388E3C))
            )
        }
    }

    // State untuk animasi tidak berubah
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) progress.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "ProgressBarAnimation"
    )
    LaunchedEffect(Unit) { animationPlayed = true }

    // Alat untuk mengukur ukuran teks
    val textMeasurer = rememberTextMeasurer()
    val percentageText = "${(progress * 100).toInt()}%"
    val textStyle =
        MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurface)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        // Latar belakang (track)
        drawRect(color = trackColor)

        // Progress bar yang terisi, menggunakan brush yang sudah ditentukan
        drawRect(
            brush = progressBarBrush, // 2. Kembali menggunakan brush
            size = Size(width = size.width * animatedProgress, height = size.height)
        )

        // Teks persentase di tengah
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

@Preview(showBackground = true)
@Composable
fun GradientLinearProgressBarPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Contoh progress rendah (gradasi hijau)
        GradientLinearProgressBar(progress = 0.3f)

        // Contoh progress menengah (gradasi kuning-oranye)
        GradientLinearProgressBar(progress = 0.7f)

        // Contoh progress tinggi (gradasi oranye-merah)
        GradientLinearProgressBar(progress = 0.9f)
    }
}