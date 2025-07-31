package com.rifqi.trackfunds.feature.transaction.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rifqi.trackfunds.feature.transaction.ui.addEdit.AddEditTransactionEvent
import com.rifqi.trackfunds.feature.transaction.ui.addEdit.TransactionItemInput

@Composable
fun TransactionDetailsSection(
    isItemsExpanded: Boolean,
    isReceiptExpanded: Boolean,
    items: List<TransactionItemInput>,
    receiptImageUri: Uri?,
    onEvent: (AddEditTransactionEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Items (Optional)",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ExpandableSection(
            isExpanded = isItemsExpanded,
            header = {
                ExpandableSectionHeader(
                    title = if (items.isNotEmpty()) "${items.size} items" else "Tap to add",
                    isExpanded = isItemsExpanded,
                    onClick = { onEvent(AddEditTransactionEvent.ToggleLineItemsSection) }
                )
            }
        ) {
            LineItemsContent(items, onEvent)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Receipt Image (Optional)",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ExpandableSection(
            isExpanded = isReceiptExpanded,
            header = {
                ExpandableSectionHeader(
                    title = if (receiptImageUri != null) "Image attached" else "Tap to add",
                    isExpanded = isReceiptExpanded,
                    onClick = { onEvent(AddEditTransactionEvent.ToggleReceiptSection) }
                )
            }
        ) {
            ReceiptContent(receiptImageUri, onEvent)
        }
    }
}

@Composable
fun ExpandableSection(
    isExpanded: Boolean,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
        label = "borderColorAnimation"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.Transparent,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            header()
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = borderColor
                    )
                    content()
                }
            }
        }
    }
}

@Composable
private fun LineItemsContent(
    lineItems: List<TransactionItemInput>,
    onEvent: (AddEditTransactionEvent) -> Unit
) {
    if (lineItems.isEmpty()) {
        Text(
            text = "No items added yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 240.dp)
        ) {
            items(
                items = lineItems,
                key = { item -> item.id }
            ) { item ->
                val index = lineItems.indexOf(item)
                LineItemRow(
                    index = index,
                    item = item,
                    onEvent = onEvent
                )
            }
        }
    }

    TextButton(onClick = { onEvent(AddEditTransactionEvent.AddNewLineItem) }) {
        Icon(Icons.Default.Add, contentDescription = "Add Item")
        Spacer(Modifier.width(4.dp))
        Text("Add New Item")
    }
}

@Composable
private fun ReceiptContent(
    receiptImageUri: Uri?,
    onEvent: (AddEditTransactionEvent) -> Unit
) {
    if (receiptImageUri != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
        ) {
            AsyncImage(
                model = receiptImageUri,
                contentDescription = "Receipt",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete Receipt Image"
                )
            }
        }
    }
    TextButton(onClick = { onEvent(AddEditTransactionEvent.OnAddReceiptClicked) }) {
        Icon(Icons.Default.AddAPhoto, contentDescription = "Add Receipt")
        Spacer(Modifier.width(4.dp))
        Text(if (receiptImageUri == null) "Add Receipt Image" else "Change Image")
    }
}


@Composable
fun ExpandableSectionHeader(
    title: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val rotationAngle by animateFloatAsState(if (isExpanded) 180f else 0f, label = "")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Toggle",
            modifier = Modifier.rotate(rotationAngle)
        )
    }
}