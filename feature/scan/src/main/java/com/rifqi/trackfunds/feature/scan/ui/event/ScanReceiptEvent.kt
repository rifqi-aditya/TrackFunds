package com.rifqi.trackfunds.feature.scan.ui.event

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.model.CategoryModel
import java.time.LocalDate

sealed interface ScanReceiptEvent {
    // Fase UPLOAD
    data object SelectFromGalleryClicked : ScanReceiptEvent
    data object SelectFromCameraClicked : ScanReceiptEvent
    data class ImageSelected(val uri: Uri) : ScanReceiptEvent
    data object ConfirmImage : ScanReceiptEvent

    // Fase REVIEW
    data class DescriptionChanged(val description: String) : ScanReceiptEvent
    data class AmountChanged(val amount: String) : ScanReceiptEvent
    data class DateChanged(val date: LocalDate) : ScanReceiptEvent
    data class AccountSelected(val account: AccountModel) : ScanReceiptEvent
    data class CategorySelected(val category: CategoryModel) : ScanReceiptEvent
    data class CategorySearchQueryChanged(val query: String) : ScanReceiptEvent
    data object AccountClicked : ScanReceiptEvent
    data object CategoryClicked : ScanReceiptEvent
    data object DateClicked : ScanReceiptEvent
    data object SaveTransactionClicked : ScanReceiptEvent
    data object ToggleItemListExpansion : ScanReceiptEvent
    data object ToggleReceiptImageExpansion : ScanReceiptEvent
    data class LineItemNameChanged(val index: Int, val newName: String) : ScanReceiptEvent
    data class LineItemQuantityChanged(val index: Int, val newQuantity: Int) : ScanReceiptEvent
    data class LineItemPriceChanged(val index: Int, val newPrice: String) : ScanReceiptEvent
    data object AddLineItem : ScanReceiptEvent
    data class DeleteLineItem(val index: Int) : ScanReceiptEvent

    // Aksi Umum
    data object ScanReceiptAgainClicked : ScanReceiptEvent
    data object DismissAllPickers : ScanReceiptEvent
}