package com.rifqi.trackfunds.feature.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.home.ui.state.HomeCategorySummaryItem

@Composable
fun CategorySummaryRow(
    categoryItem: HomeCategorySummaryItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DisplayIconFromResource(
            identifier = categoryItem.categoryIconIdentifier,
            contentDescription = categoryItem.categoryName,
            modifier = Modifier
                .size(40.dp) // Ukuran ikon bisa disesuaikan
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)) // Latar belakang ikon lebih lembut
                .padding(8.dp) // Padding di dalam background ikon
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = categoryItem.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // Agar teks mengambil sisa ruang
        )
        Text(
            text = formatCurrency(categoryItem.totalAmount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
//            color = if (categoryItem.transactionType == TransactionType.EXPENSE
//            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
        )
    }
}