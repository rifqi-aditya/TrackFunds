package com.rifqi.trackfunds.feature.transaction.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rifqi.trackfunds.feature.transaction.ui.addEditTransaction.AddEditTransactionEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.LineItemUiModel

@Composable
fun TransactionDetailsSection(
    isExpanded: Boolean,
    lineItems: List<LineItemUiModel>,
    receiptImageUri: Uri?,
    onEvent: (AddEditTransactionEvent) -> Unit
) {
    Column {
        // Tombol untuk membuka/menutup
        OutlinedButton(
            onClick = { onEvent(AddEditTransactionEvent.ToggleDetailsSection) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isExpanded) "Hide Details" else "Add Line Items & Receipt")
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Toggle Details"
            )
        }

        // Konten yang bisa diciutkan
        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- Daftar Line Items ---
                Text("Line Items", style = MaterialTheme.typography.titleMedium)
                lineItems.forEachIndexed { index, item ->
                    EditableReceiptItemRow(
                        index = index,
                        item = item,
                        onEvent = onEvent
                    )
                }
                TextButton(onClick = { onEvent(AddEditTransactionEvent.AddNewLineItem) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                    Text("Add New Item")
                }

                Divider()

                Text("Receipt", style = MaterialTheme.typography.titleMedium)
                if (receiptImageUri != null) {
                    AsyncImage(model = receiptImageUri, contentDescription = "Receipt")
                }
                TextButton(onClick = { onEvent(AddEditTransactionEvent.OnAddReceiptClicked) }) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = "Add Receipt")
                    Text(if (receiptImageUri == null) "Add Receipt Image" else "Change Image")
                }
            }
        }
    }
}