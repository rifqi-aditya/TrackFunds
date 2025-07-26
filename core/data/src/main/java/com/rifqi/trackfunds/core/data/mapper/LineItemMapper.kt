package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.LineItemEntity
import com.rifqi.trackfunds.core.domain.model.ReceiptItemModel

fun ReceiptItemModel.toEntity(transactionId: String): LineItemEntity {
    return LineItemEntity(
        transactionId = transactionId,
        name = this.name,
        quantity = this.quantity.toDouble(),
        price = this.price
    )
}

fun LineItemEntity.toDomain(): ReceiptItemModel {
    return ReceiptItemModel(
        name = this.name,
        quantity = this.quantity.toInt(),
        price = this.price
    )
}