package com.rifqi.trackfunds.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.feature.home.ui.model.HomeTransactionItem

@Composable
fun TransactionSection(
    title: String,
    items: List<HomeTransactionItem>?,
    onViewAllClick: () -> Unit,
    onItemClick: (HomeTransactionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) { // Padding atas dan bawah untuk section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            TextButton(onClick = onViewAllClick) {
                Text(
                    "View all",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (!items.isNullOrEmpty()) {
            // Menggunakan Column karena ini akan berada di dalam LazyColumn di HomeScreen
            // Jika item banyak dan butuh scroll independen (jarang untuk ringkasan), baru LazyColumn lagi
            Column(modifier = Modifier.padding(top = 4.dp)) {
                items.forEachIndexed { index, item ->
                    TransactionRow(
                        transaction = item,
                        onClick = { onItemClick(item) }
                    )
                    if (index < items.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        } else {
            Text(
                "Belum ada transaksi.", // Diubah ke Bahasa Indonesia
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}