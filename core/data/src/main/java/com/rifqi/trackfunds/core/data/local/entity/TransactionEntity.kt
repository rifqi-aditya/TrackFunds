package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SavingsGoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["savings_goal_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["user_uid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_uid", "category_id", "account_id", "transfer_pair_id", "savings_goal_id"])
    ]
)
data class TransactionEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_uid")
    val userUid: String,

    val description: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDateTime,

    @ColumnInfo(name = "receipt_image_uri")
    val receiptImageUri: String? = null,

    @ColumnInfo(name = "category_id")
    val categoryId: String?,

    @ColumnInfo(name = "account_id")
    val accountId: String,

    @ColumnInfo(name = "transfer_pair_id")
    val transferPairId: String? = null,

    @ColumnInfo(name = "savings_goal_id")
    val savingsGoalId: String? = null,
)