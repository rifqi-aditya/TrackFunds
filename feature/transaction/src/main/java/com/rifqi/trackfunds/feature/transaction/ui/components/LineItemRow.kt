package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.NumberInputField
import com.rifqi.trackfunds.feature.transaction.ui.addEdit.AddEditTransactionEvent
import com.rifqi.trackfunds.feature.transaction.ui.addEdit.TransactionItemInput

@Composable
fun LineItemRow(
    index: Int,
    item: TransactionItemInput,
    onEvent: (AddEditTransactionEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NumberInputField(
            value = item.quantity,
            onValueChange = { newQuantity ->
                onEvent(AddEditTransactionEvent.OnLineItemChanged(index, item.copy(quantity = newQuantity)))
            },
            modifier = Modifier.width(55.dp),
            label = "Qty",
        )
        GeneralTextInputField(
            value = item.name,
            onValueChange = { newName ->
                onEvent(AddEditTransactionEvent.OnLineItemChanged(index, item.copy(name = newName)))
            },
            modifier = Modifier.weight(1f),
            label = "Name"
        )
        AmountInputField(
            value = item.price,
            onValueChange = { newPrice ->
                onEvent(AddEditTransactionEvent.OnLineItemChanged(index, item.copy(price = newPrice)))
            },
            modifier = Modifier.width(120.dp),
            label = "Price",
        )
        IconButton(onClick = { onEvent(AddEditTransactionEvent.DeleteLineItem(index)) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Item")
        }
    }
}