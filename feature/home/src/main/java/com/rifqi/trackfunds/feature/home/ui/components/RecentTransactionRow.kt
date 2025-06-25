package com.rifqi.trackfunds.feature.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.util.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.util.formatCurrency
import com.rifqi.trackfunds.feature.home.ui.state.HomeCategorySummaryItem

@Composable
fun RecentTransactionRow(
    categoryItem: HomeCategorySummaryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DisplayIconFromResource(
            identifier = categoryItem.categoryIconIdentifier,
            contentDescription = categoryItem.categoryName,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = categoryItem.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatCurrency(categoryItem.totalAmount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = if (categoryItem.transactionType == TransactionType.EXPENSE
            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
        )
    }
}