package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_NULL // Jika kategori dihapus, biarkan transaksi tetap ada
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE // Jika akun dihapus, transaksinya juga terhapus
        ),
        ForeignKey(
            entity = SavingsGoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["savings_goal_id"],
            onDelete = ForeignKey.SET_NULL
        ),
    ]
)
data class TransactionEntity(
    @PrimaryKey val id: String,
    val description: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDateTime,

    @ColumnInfo(name = "category_id", index = true)
    val categoryId: String?,

    @ColumnInfo(name = "account_id", index = true)
    val accountId: String,

    @ColumnInfo(name = "transfer_pair_id", index = true)
    val transferPairId: String? = null,

    @ColumnInfo(name = "savings_goal_id", index = true)
    val savingsGoalId: String? = null
)