package com.rifqi.trackfunds.core.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Ambil teks asli (angka bersih)
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // Ubah string angka menjadi Long agar bisa diformat
        val number = originalText.toLongOrNull() ?: return TransformedText(text, OffsetMapping.Identity)

        // Format angka dengan pemisah titik
        val formatter = DecimalFormat("#,###")
        val formattedText = formatter.format(number).replace(",", ".")

        // Buat OffsetMapping untuk mengatur posisi kursor dengan benar
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Hitung berapa banyak titik yang ada sebelum posisi kursor asli
                val dotsBefore = formattedText.slice(0 until offset + (offset - 1) / 3)
                    .count { it == '.' }
                return offset + dotsBefore
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Hitung berapa banyak titik yang ada sebelum posisi kursor yang diformat
                val dotsBefore = formattedText.slice(0 until offset)
                    .count { it == '.' }
                return offset - dotsBefore
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}