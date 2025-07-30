package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.TransactionDto
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.domain.model.Account
import com.rifqi.trackfunds.core.domain.model.Category
import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.model.Transaction
import java.math.BigDecimal

/**
 * Maps a [TransactionDto] (a flat object from the database query)
 * to a nested [Transaction] domain model.
 * It safely handles cases where a transaction might not have a category or a savings goal.
 */
fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = this.transaction.id,
        description = this.transaction.description,
        amount = this.transaction.amount,
        type = this.transaction.type,
        date = this.transaction.date,
        transferPairId = this.transaction.transferPairId,

        category = this.category?.let { categoryInfo ->
            Category(
                id = this.transaction.categoryId ?: "",
                name = categoryInfo.name ?: "",
                iconIdentifier = categoryInfo.categoryIconIdentifier ?: "",
                type = transaction.type,
            )
        },

        savingsGoal = this.savingsGoal?.let { savingsInfo ->
            SavingsGoal(
                id = this.transaction.savingsGoalId ?: "",
                name = savingsInfo.name ?: "",
                iconIdentifier = savingsInfo.iconIdentifier ?: "",
                targetAmount = savingsInfo.targetAmount ?: BigDecimal.ZERO,
                savedAmount = savingsInfo.savedAmount ?: BigDecimal.ZERO,
                targetDate = savingsInfo.targetDate,
                isAchieved = savingsInfo.isAchieved ?: false
            )
        },

        account = Account(
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
fun Transaction.toEntity(userUid: String): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        userUid = userUid,
        description = this.description,
        amount = this.amount,
        type = this.type,
        date = this.date,
        categoryId = this.category?.id,
        accountId = this.account.id,
        savingsGoalId = this.savingsGoal?.id,
        transferPairId = this.transferPairId
    )
}

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = this.id,
        description = this.description,
        amount = this.amount,
        type = this.type,
        date = this.date,
        transferPairId = this.transferPairId,
        category = null, // Category will be set later when fetching details
        savingsGoal = null, // Savings goal will be set later when fetching details
        account = Account(
            id = this.accountId,
            name = "", // Name will be set later when fetching details
            iconIdentifier = "", // Icon will be set later when fetching details
            balance = BigDecimal.ZERO // Balance will be set later when fetching details
        )
    )
}
