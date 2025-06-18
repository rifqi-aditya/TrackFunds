package com.rifqi.trackfunds.feature.transaction.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTypeToggleButtons(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val types = listOf(TransactionType.EXPENSE, TransactionType.INCOME) // Gunakan enum
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        types.forEach { type ->
            val isSelected = type == selectedType
            FilterChip(
                selected = isSelected,
                onClick = { onTypeSelected(type) },
                label = {
                    Text(
                        // Mengkapitalisasi huruf pertama dari nama enum
                        type.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                        }
                    )
                },
                shape = MaterialTheme.shapes.medium,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                ),
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null,
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    selectedBorderColor = Color.Transparent
                )
            )
        }
    }
}

// --- PREVIEWS UNTUK TRANSACTIONTYPETOGGLEBUTTONS ---

@Preview(showBackground = true, name = "Toggle Buttons - Expense Selected")
@Composable
fun TransactionTypeToggleButtonsExpensePreview() {
    TrackFundsTheme {
        Surface {
            // State lokal untuk mengelola pilihan di dalam preview
            var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
            TransactionTypeToggleButtons(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Toggle Buttons - Income Selected", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TransactionTypeToggleButtonsIncomePreview() {
    TrackFundsTheme(darkTheme = true) {
        Surface {
            // State lokal untuk mengelola pilihan di dalam preview
            var selectedType by remember { mutableStateOf(TransactionType.INCOME) }
            TransactionTypeToggleButtons(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// Preview untuk menunjukkan kedua state dalam satu tampilan
@Preview(showBackground = true, name = "Toggle Buttons - Both States")
@Composable
fun TransactionTypeToggleButtonsAllStatesPreview() {
    TrackFundsTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // State di mana Expense dipilih
            Text("Expense Selected:", style = MaterialTheme.typography.labelSmall)
            TransactionTypeToggleButtons(
                selectedType = TransactionType.EXPENSE,
                onTypeSelected = {}
            )
            // State di mana Income dipilih
            Text("Income Selected:", style = MaterialTheme.typography.labelSmall)
            TransactionTypeToggleButtons(
                selectedType = TransactionType.INCOME,
                onTypeSelected = {}
            )
        }
    }
}