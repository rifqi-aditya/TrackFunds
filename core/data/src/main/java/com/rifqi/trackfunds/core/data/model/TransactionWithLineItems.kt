package com.rifqi.trackfunds.core.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.rifqi.trackfunds.core.data.local.entity.LineItemEntity
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity

data class TransactionWithLineItems(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "transaction_id"
    )
    val lineItems: List<LineItemEntity>
)