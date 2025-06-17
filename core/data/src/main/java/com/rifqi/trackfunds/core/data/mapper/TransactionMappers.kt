package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.CategoryTransactionSummaryDto
import com.rifqi.trackfunds.core.data.local.dto.TransactionDetailDto
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem

/**
 * Mapper dari TransactionDetailDto (hasil query Room) ke TransactionItem (domain model).
 */
fun TransactionDetailDto.toDomain(): TransactionItem {
    return TransactionItem(
        id = this.transaction.id,
        note = this.transaction.note,
        amount = this.transaction.amount,
        type = this.transaction.type,
        date = this.transaction.date,
        categoryId = this.transaction.categoryId ?: "",
        categoryName = this.category.name,
        iconIdentifier = this.category.iconIdentifier,
        accountId = this.transaction.accountId,
        accountName = this.account.name
    )
}

/**
 * Mapper dari TransactionItem (domain) ke TransactionEntity (Room).
 * Dibutuhkan saat menyimpan atau mengupdate transaksi.
 */
fun TransactionItem.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        note = this.note,
        amount = this.amount,
        type = this.type,
        date = this.date,
        categoryId = this.categoryId,
        accountId = this.accountId
    )
}


fun CategoryTransactionSummaryDto.toDomain(): CategorySummaryItem {
    return CategorySummaryItem(
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        categoryIconIdentifier = this.categoryIconIdentifier,
        transactionType = this.transactionType,
        totalAmount = this.totalAmount,
    )
}