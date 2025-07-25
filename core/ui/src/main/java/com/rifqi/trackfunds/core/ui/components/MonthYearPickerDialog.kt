package com.rifqi.trackfunds.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@Composable
private fun YearSelector(
    year: Int,
    onYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onYearChange(year - 1) }) {
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = "Previous Year")
        }
        Text(
            text = year.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = { onYearChange(year + 1) }) {
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = "Next Year")
        }
    }
}

@Composable
private fun MonthSelector(
    selectedMonth: Month,
    onMonthSelected: (Month) -> Unit,
    modifier: Modifier = Modifier
) {
    val months = Month.entries.toTypedArray()
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(months) { month ->
            TextButton(
                onClick = { onMonthSelected(month) },
                colors = if (month == selectedMonth) {
                    ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                } else {
                    ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                }
            ) {
                Text(month.name.take(3)) // Ambil 3 huruf pertama, e.g., "JAN"
            }
        }
    }
}


@Composable
fun MonthYearPickerDialog(
    showDialog: Boolean,
    initialYearMonth: YearMonth,
    onDismiss: () -> Unit,
    onConfirm: (YearMonth) -> Unit
) {
    if (showDialog) {
        var selectedYear by remember { mutableIntStateOf(initialYearMonth.year) }
        var selectedMonth by remember { mutableStateOf(initialYearMonth.month) }

        val currentSelection = YearMonth.of(selectedYear, selectedMonth)
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = currentSelection.format(formatter),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    YearSelector(
                        year = selectedYear,
                        onYearChange = { selectedYear = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    MonthSelector(
                        selectedMonth = selectedMonth,
                        onMonthSelected = { selectedMonth = it }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirm(currentSelection) }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true, name = "Month Year Picker Dialog")
@Composable
private fun MonthYearPickerDialogPreview() {
    TrackFundsTheme {
        // Kita panggil dialognya secara langsung dengan showDialog = true
        // agar ia langsung tampil di panel preview.
        MonthYearPickerDialog(
            showDialog = true,
            // Gunakan tanggal spesifik agar preview konsisten
            initialYearMonth = YearMonth.of(2025, Month.JUNE),
            onDismiss = {}, // Aksi kosong untuk preview
            onConfirm = {}  // Aksi kosong untuk preview
        )
    }
}

