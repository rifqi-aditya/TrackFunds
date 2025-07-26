package com.rifqi.trackfunds.feature.scan.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Menampilkan indikator langkah (step) untuk alur multi-fase.
 * @param currentStep Indeks dari langkah saat ini (dimulai dari 0).
 * @param steps Daftar nama-nama langkah.
 */
@Composable
fun StepIndicator(
    currentStep: Int,
    steps: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, title ->
            // Menampilkan satu langkah (lingkaran + teks)
            Step(
                title = title,
                stepNumber = index + 1,
                isActive = index == currentStep,
                isCompleted = index < currentStep
            )

            // Menampilkan garis pemisah, kecuali untuk langkah terakhir
            if (index < steps.lastIndex) {
                val color by animateColorAsState(
                    targetValue = if (index < currentStep) MaterialTheme.colorScheme.primary else Color.Gray,
                    animationSpec = tween(300),
                    label = "divider_color"
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 2.dp,
                    color = color
                )
            }
        }
    }
}

@Composable
private fun Step(
    title: String,
    stepNumber: Int,
    isActive: Boolean,
    isCompleted: Boolean
) {
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = Color.Gray

    val circleColor by animateColorAsState(
        targetValue = if (isActive || isCompleted) activeColor else inactiveColor,
        animationSpec = tween(300),
        label = "circle_color"
    )
    val textColor by animateColorAsState(
        targetValue = if (isActive || isCompleted) activeColor else inactiveColor,
        animationSpec = tween(300),
        label = "text_color"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(circleColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$stepNumber",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}