package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.CategoryTransactionSummaryDto
import com.rifqi.trackfunds.core.data.local.dto.TransactionDetailDto
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem

/**
 * Maps a [TransactionDetailDto] (a flat object from the database query)
 * to a nested [TransactionItem] domain model.
 * It safely handles cases where a transaction might not have a category or a savings goal.
 */
fun TransactionDetailDto.toDomain(): TransactionItem {
    // 1. Ambil data transaksi dasar
    val transactionEntity = this.transaction

    // 2. Buat objek CategoryItem hanya jika data kategori tidak null
    val categoryItem = this.category?.let { categoryInfo ->
        transactionEntity.categoryId?.let { categoryId ->
            CategoryItem(
                id = categoryId,
                name = categoryInfo.name,
                iconIdentifier = categoryInfo.categoryIconIdentifier,
                type = transactionEntity.type
            )
        }
    }

    // 3. Buat objek SavingsGoalItem hanya jika data tabungan tidak null
    val savingsGoalItem = this.savingsGoal?.let { savingsInfo ->
        transactionEntity.savingsGoalId?.let { goalId ->
            SavingsGoalItem(
                id = goalId,
                name = savingsInfo.name,
                iconIdentifier = savingsInfo.iconIdentifier,
                targetAmount = savingsInfo.targetAmount.toBigDecimal(),
                currentAmount = savingsInfo.currentAmount.toBigDecimal(),
                targetDate = savingsInfo.targetDate,
                isAchieved = savingsInfo.isAchieved
            )
        }
    }

    // 4. Buat objek TransactionItem final
    return TransactionItem(
        id = transactionEntity.id,
        description = transactionEntity.description,
        amount = transactionEntity.amount,
        type = transactionEntity.type,
        date = transactionEntity.date,
        transferPairId = transactionEntity.transferPairId,
        category = categoryItem,
        savingsGoalItem = savingsGoalItem,
        // Account selalu ada karena menggunakan INNER JOIN
        account = AccountItem(
            id = transactionEntity.accountId,
            name = this.account.name,
            iconIdentifier = this.account.accountIconIdentifier,
            balance = this.account.balance
        )
    )
}

/**
 * Maps a TransactionItem (domain model) to a TransactionEntity (Room entity).
 * Required when saving or updating a transaction.
 */
fun TransactionItem.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        description = this.description,
        amount = this.amount,
        type = this.type,
        date = this.date,
        categoryId = this.category?.id,
        accountId = this.account.id,
        savingsGoalId = this.savingsGoalItem?.id,
        transferPairId = this.transferPairId
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