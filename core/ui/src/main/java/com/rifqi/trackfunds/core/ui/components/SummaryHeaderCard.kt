package com.rifqi.trackfunds.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

@Composable
fun SummaryHeaderCard(
    totalExpenses: BigDecimal,
    totalIncome: BigDecimal, // Tambahkan total income untuk menghitung progres
    modifier: Modifier = Modifier
) {
    // Hitung progres. Jika tidak ada income, progres 0.
    // Pastikan progress antara 0.0 dan 1.0
    val progress = if (totalIncome > BigDecimal.ZERO) {
        (totalExpenses / totalIncome).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest) // Warna background yang lebih menonjol
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Jarak antar elemen di dalam kartu
        ) {
            // Baris Judul
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Pengeluaran Bulan Ini",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Info, // Contoh ikon
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Jumlah Pengeluaran
            Text(
                text = formatCurrency(totalExpenses),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error, // Gunakan warna error untuk menekankan pengeluaran
                fontWeight = FontWeight.Bold
            )

            // Bilah Progres
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape), // Membuat ujung progress bar rounded
                trackColor = MaterialTheme.colorScheme.surfaceVariant, // Warna background bar
                color = MaterialTheme.colorScheme.error // Warna progres
            )

            // Teks Konteks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "dari ${formatCurrency(totalIncome)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    // Tampilkan persentase
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}