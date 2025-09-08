package com.rifqi.trackfunds.feature.home.ui.settings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.R

private data class LanguageOption(@StringRes val labelResId: Int, val tag: String)

@Composable
fun LanguagePickerDialog(
    showDialog: Boolean,
    currentTag: String,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    if (!showDialog) return

    val languageOptions = remember {
        listOf(
            LanguageOption(R.string.language_follow_system, ""),
            LanguageOption(R.string.english, "en"),
            LanguageOption(R.string.indonesian, "id")
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.language)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                languageOptions.forEach { option ->
                    LanguageRow(
                        selected = currentTag == option.tag,
                        label = stringResource(id = option.labelResId),
                        onClick = { onLanguageSelected(option.tag) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}

@Composable
private fun LanguageRow(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
