package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.CategoryTransactionSummaryDto
import com.rifqi.trackfunds.core.data.local.dto.TransactionDetailDto
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.Transaction
import java.math.BigDecimal

/**
 * Maps a [TransactionDetailDto] (a flat object from the database query)
 * to a nested [Transaction] domain model.
 * It safely handles cases where a transaction might not have a category or a savings goal.
 */
fun TransactionDetailDto.toDomain(): Transaction {
    return Transaction(
        id = this.transaction.id,
        description = this.transaction.description,
        amount = this.transaction.amount,
        type = this.transaction.type,
        date = this.transaction.date,
        transferPairId = this.transaction.transferPairId,

        category = this.category?.let { categoryInfo ->
            CategoryItem(
                id = this.transaction.categoryId ?: "",
                name = categoryInfo.name ?: "",
                iconIdentifier = categoryInfo.categoryIconIdentifier ?: "",
                type = transaction.type,
            )
        },

        savingsGoalItem = this.savingsGoal?.let { savingsInfo ->
            SavingsGoalItem(
                id = this.transaction.savingsGoalId ?: "",
                name = savingsInfo.name ?: "",
                iconIdentifier = savingsInfo.iconIdentifier ?: "",
                targetAmount = savingsInfo.targetAmount ?: BigDecimal.ZERO,
                currentAmount = savingsInfo.currentAmount ?: BigDecimal.ZERO,
                targetDate = savingsInfo.targetDate,
                isAchieved = savingsInfo.isAchieved ?: false
            )
        },

        account = AccountItem(
            id = this.transaction.accountId,
            name = this.account.name,
            iconIdentifier = this.account.accountIconIdentifier,
            balance = this.account.balance ?: BigDecimal.ZERO
        )
    )
}

/**
 * Maps a TransactionItem (domain model) to a TransactionEntity (Room entity).
 * Required when saving or updating a transaction.
 */
fun Transaction.toEntity(): TransactionEntity {
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