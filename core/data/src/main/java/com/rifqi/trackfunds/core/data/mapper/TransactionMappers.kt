package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.TransactionDto
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.model.CategoryModel
import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import com.rifqi.trackfunds.core.domain.model.TransactionModel
import java.math.BigDecimal

/**
 * Maps a [TransactionDto] (a flat object from the database query)
 * to a nested [TransactionModel] domain model.
 * It safely handles cases where a transaction might not have a category or a savings goal.
 */
fun TransactionDto.toDomain(): TransactionModel {
    return TransactionModel(
        id = this.transaction.id,
        description = this.transaction.description,
        amount = this.transaction.amount,
        type = this.transaction.type,
        date = this.transaction.date,
        transferPairId = this.transaction.transferPairId,

        category = this.category?.let { categoryInfo ->
            CategoryModel(
                id = this.transaction.categoryId ?: "",
                name = categoryInfo.name ?: "",
                iconIdentifier = categoryInfo.categoryIconIdentifier ?: "",
                type = transaction.type,
            )
        },

        savingsGoalModel = this.savingsGoal?.let { savingsInfo ->
            SavingsGoalModel(
                id = this.transaction.savingsGoalId ?: "",
                name = savingsInfo.name ?: "",
                iconIdentifier = savingsInfo.iconIdentifier ?: "",
                targetAmount = savingsInfo.targetAmount ?: BigDecimal.ZERO,
                currentAmount = savingsInfo.currentAmount ?: BigDecimal.ZERO,
                targetDate = savingsInfo.targetDate,
                isAchieved = savingsInfo.isAchieved ?: false
            )
        },

        account = AccountModel(
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
fun TransactionModel.toEntity(userUid: String): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        userUid = userUid,
        description = this.description,
        amount = this.amount,
        type = this.type,
        date = this.date,
        categoryId = this.category?.id,
        accountId = this.account.id,
        savingsGoalId = this.savingsGoalModel?.id,
        transferPairId = this.transferPairId
    )
}
