package com.rifqi.trackfunds.feature.budget.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

@Composable
fun BudgetRingChart(
    progress: Float,
    remainingAmount: BigDecimal,
    totalAmount: BigDecimal,
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

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Teks di tengah chart (tidak berubah)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatCurrency(remainingAmount),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Remaining from ${formatCurrency(totalAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "June 2025 - June 2025",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        CircularProgressBar(
            progress = currentProgress,
            modifier = Modifier
                .fillMaxSize()
                .size(size - strokeWidth * 2f)
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
                remainingAmount = BigDecimal("100000"),
                totalAmount = BigDecimal("1000000")
            )
        }
    }
}