package com.rifqi.trackfunds.feature.scan.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

/**
 * Menampilkan indikator langkah (step) menggunakan LinearProgressIndicator.
 * @param currentStep Indeks dari langkah saat ini (dimulai dari 0).
 * @param steps Daftar nama-nama langkah.
 */
@Composable
fun LinearStepIndicator(
    currentStep: Int,
    steps: List<String>,
    modifier: Modifier = Modifier
) {
    val totalSteps = steps.size
    // Menghitung progres dalam format 0.0f hingga 1.0f
    // Kita tambahkan 1 ke currentStep agar langkah pertama (indeks 0) sudah memiliki progres.
    val progress = (currentStep + 1) / totalSteps.toFloat()

    // Menambahkan animasi agar perubahan progres terlihat mulus
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "progressAnimation"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        // Baris untuk menampilkan label teks
        Row(modifier = Modifier.fillMaxWidth()) {
            steps.forEachIndexed { index, title ->
                val isCompletedOrActive = index <= currentStep
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (index == currentStep) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCompletedOrActive) TrackFundsTheme.extendedColors.accent else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Progress bar
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            color = TrackFundsTheme.extendedColors.accent
        )
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun LinearStepIndicatorPreview() {
    TrackFundsTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val steps = listOf("Upload", "Preview", "Processing", "Review")

            // Contoh untuk setiap langkah
            LinearStepIndicator(currentStep = 0, steps = steps)
            LinearStepIndicator(currentStep = 1, steps = steps)
            LinearStepIndicator(currentStep = 2, steps = steps)
            LinearStepIndicator(currentStep = 3, steps = steps)
        }
    }
}