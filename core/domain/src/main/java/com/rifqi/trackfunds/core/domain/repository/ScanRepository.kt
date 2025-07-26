package com.rifqi.trackfunds.core.domain.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.ScanResult

interface ScanRepository {
    /**
     * Extracts raw text from an image using on-device OCR.
     */
    suspend fun extractTextFromImage(imageUri: Uri): Result<String>

    /**
     * Analyzes extracted text with Gemini to get structured receipt data.
     */
    suspend fun analyzeReceiptText(ocrText: String): Result<ScanResult>
}