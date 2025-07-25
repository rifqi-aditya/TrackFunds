package com.rifqi.trackfunds.core.ui.components.inputfield

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal

enum class DatePickerMode {
    FULL_DATE,
    MONTH_YEAR_ONLY
}

@Composable
fun DatePickerField(
    label: String,
    value: Temporal?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mode: DatePickerMode,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val (formatter, placeholder) = when (mode) {
        DatePickerMode.FULL_DATE -> {
            DateTimeFormatter.ofPattern("dd MMMM yyyy") to "Choose Date"
        }

        DatePickerMode.MONTH_YEAR_ONLY -> {
            DateTimeFormatter.ofPattern("MMMM yyyy") to "Choose Month/Year"
        }
    }

    val displayValue = when (value) {
        is LocalDate -> value.format(formatter)
        is YearMonth -> value.format(formatter)
        else -> placeholder
    }

    val borderColor = if (isError) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.outline
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = MaterialTheme.shapes.large
                )
                .clip(MaterialTheme.shapes.large)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ikon Kalender dan Teks Tanggal
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    DisplayIconFromResource(
                        identifier = "calendar",
                        contentDescription = "Pilih Tanggal",
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = displayValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (value != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "DatePickerField - Month/Year Selected") // Nama diubah
@Composable
private fun DatePickerFieldSelectedMonthYearPreview() { // Nama diubah
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DatePickerField(
                label = "Periode Budget",
                value = YearMonth.now(), // DIUBAH: Gunakan YearMonth untuk mode ini
                onClick = {},
                mode = DatePickerMode.MONTH_YEAR_ONLY,
            )
        }
    }
}

// Preview untuk FULL_DATE tetap menggunakan LocalDate
@Preview(showBackground = true, name = "DatePickerField - Full Date Selected")
@Composable
private fun DatePickerFieldSelectedFullDatePreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DatePickerField(
                label = "Tanggal Transaksi",
                value = LocalDate.now(),
                onClick = {},
                mode = DatePickerMode.FULL_DATE,
            )
        }
    }
}