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
import java.time.format.DateTimeFormatter

@Composable
fun DatePickerField(
    label: String,
    value: LocalDate?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val displayValue = value?.format(formatter) ?: "Pilih Tanggal"
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
    }
}

@Preview(showBackground = true, name = "DatePickerField - Not Selected")
@Composable
private fun DatePickerFieldNotSelectedPreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DatePickerField(
                label = "Target Tanggal",
                value = null, // Kondisi belum ada tanggal dipilih
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "DatePickerField - Selected")
@Composable
private fun DatePickerFieldSelectedPreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DatePickerField(
                label = "Target Tanggal",
                value = LocalDate.now(), // Kondisi sudah ada tanggal dipilih
                onClick = {}
            )
        }
    }
}