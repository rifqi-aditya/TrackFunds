package com.rifqi.trackfunds.core.ui.components.inputfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

/**
 * A general-purpose text input field that can be used for single-line or multi-line text.
 *
 * @param value The input text to be shown in the text field.
 * @param onValueChange The callback that is triggered when the input service updates the text.
 * @param label The label to be displayed above the text field.
 * @param placeholder The placeholder to be displayed when the text field is empty.
 * @param singleLine True if this is a single-line field; false for multi-line.
 * @param modifier The Modifier to be applied to this layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralTextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(), // Modifier dari parameter tidak digunakan di sini, agar tidak duplikat
            placeholder = { Text(placeholder) },
            singleLine = singleLine,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = keyboardOptions,
            isError = isError,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "General Text Input Fields")
@Composable
private fun GeneralTextInputFieldPreview() {
    TrackFundsTheme {
        var goalName by remember { mutableStateOf("Liburan ke Jepang") }
        var description by remember { mutableStateOf("Tiket pesawat, hotel, dan uang saku untuk 7 hari.") }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Contoh penggunaan untuk input satu baris (Nama Tabungan)
            GeneralTextInputField(
                value = goalName,
                onValueChange = { goalName = it },
                label = "Nama Tujuan",
                placeholder = "Contoh: Dana Darurat",
                singleLine = true
            )

            GeneralTextInputField(
                value = "",
                onValueChange = { },
                label = "Nama Tujuan",
                isError = true,
                errorMessage = "Nama tujuan tidak boleh kosong."
            )
        }
    }
}