package com.rifqi.trackfunds.core.domain.scan.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.scan.model.ScanResult

interface ScanRepository {
    /**
     * Extracts raw text from an image using on-device OCR.
     */
    suspend fun extractTextFromImage(imageUri: Uri): Result<String>

    /**
     * Analyzes extracted text with Gemini to get structured receipt data.
     */
    suspend fun analyzeReceiptText(ocrText: String, userUid: String): Result<ScanResult>
}