package com.rifqi.trackfunds.core.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity

data class TransactionDetailDto(
    @Embedded
    val transaction: TransactionEntity,

    @Embedded(prefix = "category_")
    val category: CategoryInfo,

    @Embedded(prefix = "account_")
    val account: AccountInfo
) {
    data class CategoryInfo(
        val name: String,
        @ColumnInfo(name = "icon_identifier")
        val categoryIconIdentifier: String?
    )

    data class AccountInfo(
        val name: String,
        @ColumnInfo(name = "icon_identifier")
        val accountIconIdentifier: String?
    )
}