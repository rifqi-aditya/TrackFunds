package com.rifqi.add_transaction.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountInputForm(
    value: String,
    onValueChange: (String) -> Unit,
//    onCalculatorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Amount",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
        )
        // Menggunakan TextField biasa untuk tampilan yang lebih clean, dengan underline saat fokus
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0", style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)) },
            leadingIcon = {
                Text(
                    "Rp",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 12.dp) // Padding agar sejajar dengan teks input
                )
            },
//            trailingIcon = {
//                IconButton(onClick = onCalculatorClick) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_account_balance_wallet), // Pastikan ikon ini ada di :core:ui
//                        contentDescription = "Kalkulator",
//                        modifier = Modifier.size(24.dp),
//                        tint = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            },
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                textAlign = TextAlign.Start, // Sejajarkan kiri agar Rp dan angka berdekatan
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword), // NumberPassword agar hanya angka tanpa simbol lain
            singleLine = true,
            shape = RoundedCornerShape(16.dp), // Tetap rounded
            colors = TextFieldDefaults.colors( // TextField biasa tanpa outline
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Underline saat fokus
                unfocusedIndicatorColor = Color.Transparent, // Tanpa underline saat tidak fokus
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}