package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.AccountEntity
import com.rifqi.trackfunds.core.domain.model.AccountItem

// Mapper dari AccountEntity (data layer) ke AccountItem (domain layer)
fun AccountEntity.toDomain(): AccountItem {
    return AccountItem(
        id = this.id,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        balance = this.balance
    )
}

// Mapper dari AccountItem (domain) ke AccountEntity (data)
fun AccountItem.toEntity(): AccountEntity {
    return AccountEntity(
        id = this.id,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        balance = this.balance
    )
}