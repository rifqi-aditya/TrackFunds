package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithDetails(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity?,

    @Relation(
        parentColumn = "account_id",
        entityColumn = "id"
    )
    val account: AccountEntity,

    @Relation(
        parentColumn = "savings_goal_id",
        entityColumn = "id"
    )
    val savingsGoal: SavingsGoalEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "transaction_id"
    )
    val items: List<TransactionItemEntity>
)