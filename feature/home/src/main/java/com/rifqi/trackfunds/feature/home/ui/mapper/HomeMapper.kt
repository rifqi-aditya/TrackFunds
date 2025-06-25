package com.rifqi.trackfunds.feature.home.ui.mapper

import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.feature.home.ui.state.HomeCategorySummaryItem

fun CategorySummaryItem.toHomeCategorySummary(): HomeCategorySummaryItem {
    return HomeCategorySummaryItem(
        categoryName = this.categoryName,
        categoryIconIdentifier = this.categoryIconIdentifier,
        totalAmount = this.totalAmount,
        transactionType = this.transactionType,
        categoryId = this.categoryId,
    )
}
