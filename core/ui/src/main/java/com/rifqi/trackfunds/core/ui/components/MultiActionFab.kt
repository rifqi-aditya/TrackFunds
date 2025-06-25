package com.rifqi.trackfunds.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun MultiActionFab(
    onScanClick: () -> Unit,
    onAddManuallyClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        label = "fabRotation"
    )

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mini FABs hanya akan muncul jika isExpanded == true
        AnimatedVisibility(visible = isExpanded) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FabItem(
                    icon = Icons.Default.DocumentScanner,
                    label = "Scan Receipt",
                    onClick = { onScanClick(); isExpanded = false })
                FabItem(
                    icon = Icons.Default.Edit,
                    label = "Add Manually",
                    onClick = { onAddManuallyClick(); isExpanded = false })
            }
        }

        // FAB Utama
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded }
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Transaction",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
private fun FabItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(shape = MaterialTheme.shapes.small) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
        SmallFloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Icon(imageVector = icon, contentDescription = label)
        }
    }
}