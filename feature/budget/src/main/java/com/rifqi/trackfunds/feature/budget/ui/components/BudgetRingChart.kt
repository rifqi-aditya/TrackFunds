package com.rifqi.trackfunds.feature.budget.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@Composable
fun BudgetRingChart(
    progress: Float,
    size: Dp = 200.dp,
    strokeWidth: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val currentProgress by animateFloatAsState(
        targetValue = if (animationPlayed) progress.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "progressAnimation"
    )
    LaunchedEffect(Unit) { animationPlayed = true }

    val percentageValue = progress * 100f
    val percentageText = "%.1f%% Used".format(percentageValue)

    // 2. Tentukan teks status dan warnanya berdasarkan persentase
    val (statusText, statusColor) = when {
        progress > 1f -> "Over Budget" to TrackFundsTheme.extendedColors.expense
        progress >= 0.9f -> "Nearing Limit" to Color.Yellow
        progress >= 0.5f -> "On Track" to TrackFundsTheme.extendedColors.income
        else -> "Safe to Spend" to TrackFundsTheme.extendedColors.income
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = percentageText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
        CircularProgressBar(
            progress = currentProgress,
            modifier = Modifier
                .fillMaxSize()
                .size(size - strokeWidth * 2f),
            strokeWidth = strokeWidth
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun BudgetRingChartPreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BudgetRingChart(
                progress = 0.75f,
            )
        }
    }
}