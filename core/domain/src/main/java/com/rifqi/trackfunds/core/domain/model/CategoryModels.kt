package com.rifqi.trackfunds.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

enum class TransactionType {
    EXPENSE, INCOME
}

@Parcelize
data class CategoryItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconIdentifier: String,
    val type: TransactionType,
) : Parcelable