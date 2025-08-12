package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SingleChoiceSheet(
    title: String,
    options: List<T>,
    selected: T?,
    onOptionSelected: (T) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    key: (T) -> String = { it.hashCode().toString() },
    primaryLabel: (T) -> String,
    secondaryLabel: (T) -> String? = { null },
    leading: @Composable (isSelected: Boolean) -> Unit = { isSelected ->
        CheckCircle(selected = isSelected)
    }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Divider()

        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = options,
                key = { key(it) }
            ) { option ->
                val isSelected = selected == option
                val primary = primaryLabel(option)
                val secondary = secondaryLabel(option)

                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOptionSelected(option) }
                        .padding(horizontal = 8.dp),
                    leadingContent = { leading(isSelected) },
                    headlineContent = {
                        Text(
                            text = primary,
                            color = if (isSelected) TrackFundsTheme.extendedColors.accent
                            else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    supportingContent = {
                        if (secondary != null) {
                            Text(
                                text = secondary,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun CheckCircle(selected: Boolean, modifier: Modifier = Modifier) {
    val bg = if (selected) TrackFundsTheme.extendedColors.accent else Color.Transparent
    val borderColor = if (selected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant
    val iconTint = if (selected) TrackFundsTheme.extendedColors.onAccent else Color.Transparent

    Box(
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(bg)
            .border(width = 1.dp, color = borderColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = if (selected) "Selected" else "Not selected",
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
    }
}

