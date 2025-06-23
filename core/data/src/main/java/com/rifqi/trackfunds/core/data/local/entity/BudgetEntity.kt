package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE // Jika kategori dihapus, budgetnya juga hilang
        )
    ]
)
data class BudgetEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "category_id", index = true)
    val categoryId: String,

    // Jumlah yang dianggarkan
    val amount: BigDecimal,

    // Periode budget, contoh: "2025-06"
    val period: String
)