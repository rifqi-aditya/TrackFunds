package com.rifqi.trackfunds.feature.home.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.components.GradientLinearProgressBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

// Data class untuk status budget, agar kode lebih bersih
private data class BudgetStatus(val text: String, val color: Color)

@Composable
fun BudgetCard(
    spentAmount: BigDecimal,
    remainingAmount: BigDecimal,
    progress: Float,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Menentukan status dan warna berdasarkan progress
    val status = remember(progress) {
        when {
            progress > 1f -> BudgetStatus("Melebihi Batas!", Color.Red)
            progress > 0.8f -> BudgetStatus("Hati-hati", Color(0xFFFFA726)) // Orange
            else -> BudgetStatus("Aman", Color(0xFF2E7D32)) // Green
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Baris Judul dan Tombol Detail
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "This Month's Budget",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "See All",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            GradientLinearProgressBar(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Baris Informasi Nominal (Digunakan & Sisa)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Used",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatCurrency(spentAmount),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Remaining",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatCurrency(remainingAmount),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// --- Preview untuk berbagai skenario ---
@Preview(showBackground = true)
@Composable
fun BudgetCardPreview() {
    TrackFundsTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Skenario 1: Budget Aman
            BudgetCard(
                spentAmount = BigDecimal("2500000"),
                remainingAmount = BigDecimal("5000000"),
                onDetailsClick = {},
                progress = 0.5f // 50% digunakan
            )

            // Skenario 2: Budget Hati-hati
            BudgetCard(
                spentAmount = BigDecimal("4200000"),
                remainingAmount = BigDecimal("800000"),
                onDetailsClick = {},
                progress = 0.84f // 84% digunakan
            )

            // Skenario 3: Budget Melebihi Batas
            BudgetCard(
                spentAmount = BigDecimal("5500000"),
                remainingAmount = BigDecimal("-500000"),
                onDetailsClick = {},
                progress = 1.1f // 110% digunakan
            )
        }
    }
}