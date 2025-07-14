package com.rifqi.trackfunds.core.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // Hindari error jika teks tidak bisa di-parse menjadi Long
        val number = try {
            originalText.toLong()
        } catch (e: NumberFormatException) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formatter = DecimalFormat("#,###")
        val formattedText = formatter.format(number).replace(",", ".")

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val dotsBefore = formattedText.take(offset + (offset - 1) / 3)
                    .count { it == '.' }
                return (offset + dotsBefore).coerceAtMost(formattedText.length)
            }
            override fun transformedToOriginal(offset: Int): Int {
                val dotsBefore = formattedText.take(offset)
                    .count { it == '.' }
                return (offset - dotsBefore).coerceAtLeast(0)
            }
        }
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}