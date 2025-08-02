package com.rifqi.trackfunds.feature.scan.ui.state

import android.net.Uri
import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.scan.model.ScanResult
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction

enum class ScanPhase {
    UPLOAD,
    IMAGE_PREVIEW,
    PROCESSING,
//    REVIEW
}

enum class ScanSheetType {
    CATEGORY, ACCOUNT
}


data class ScanReceiptUiState(
    val currentPhase: ScanPhase = ScanPhase.UPLOAD,
    val imagePreviewUri: Uri? = null,
    val scanResult: ScanResult? = null,

    // Data transaksi yang sedang diedit oleh pengguna
    val editableTransaction: Transaction? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Flags untuk UI
    val isItemListExpanded: Boolean = false,
    val isReceiptImageExpanded: Boolean = false,
    val activeSheet: ScanSheetType? = null,
    val showDatePicker: Boolean = false,
    val categorySearchQuery: String = "",

    val allAccounts: List<Account> = emptyList(),
)