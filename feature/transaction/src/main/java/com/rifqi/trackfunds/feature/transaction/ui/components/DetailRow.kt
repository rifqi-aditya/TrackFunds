package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable untuk menampilkan satu baris detail,
 * terdiri dari label di kiri dan konten nilai di kanan.
 * @param label Teks yang akan ditampilkan sebagai label di sebelah kiri.
 * @param valueContent Slot untuk Composable yang akan ditampilkan sebagai nilai di sebelah kanan.
 */
@Composable
fun DetailRow(
    label: String,
    modifier: Modifier = Modifier,
    valueContent: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        valueContent()
    }
}