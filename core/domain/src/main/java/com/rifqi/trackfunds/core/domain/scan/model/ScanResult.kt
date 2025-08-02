package com.rifqi.trackfunds.core.domain.scan.model

import android.net.Uri
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionItem
import java.math.BigDecimal
import java.time.LocalDateTime

data class ScanResult(
    val merchantName: String?,
    val transactionDateTime: LocalDateTime,
    val totalAmount: BigDecimal,
    val categoryStandardKey: String?,
    val transactionItem: List<TransactionItem> = emptyList(),
    val receiptImageUri: Uri? = null,
)