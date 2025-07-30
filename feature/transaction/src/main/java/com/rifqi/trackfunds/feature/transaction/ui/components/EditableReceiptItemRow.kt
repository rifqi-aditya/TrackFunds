package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.feature.transaction.ui.addEditTransaction.AddEditTransactionEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.LineItemUiModel

@Composable
fun EditableReceiptItemRow(
    index: Int,
    item: LineItemUiModel,
    onEvent: (AddEditTransactionEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = item.quantity,
            onValueChange = { newQuantity ->
                if (newQuantity.all { it.isDigit() }) {
                    val updatedItem = item.copy(quantity = newQuantity)
                    onEvent(AddEditTransactionEvent.OnLineItemChanged(index, updatedItem))
                }
            },
            modifier = Modifier.width(60.dp),
            label = { Text("Qty") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = item.name,
            onValueChange = { newName ->
                val updatedItem = item.copy(name = newName)
                onEvent(AddEditTransactionEvent.OnLineItemChanged(index, updatedItem))
            },
            modifier = Modifier.weight(1f),
            label = { Text("Item Name") }
        )
        OutlinedTextField(
            value = item.price,
            onValueChange = { newPrice ->
                // Hanya izinkan angka
                if (newPrice.all { it.isDigit() }) {
                    val updatedItem = item.copy(price = newPrice)
                    onEvent(AddEditTransactionEvent.OnLineItemChanged(index, updatedItem))
                }
            },
            modifier = Modifier.width(100.dp),
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        IconButton(onClick = { onEvent(AddEditTransactionEvent.DeleteLineItem(index)) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Item")
        }
    }
}