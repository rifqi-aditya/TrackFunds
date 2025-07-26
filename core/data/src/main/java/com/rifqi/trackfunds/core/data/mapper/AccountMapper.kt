package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.AccountEntity
import com.rifqi.trackfunds.core.domain.model.AccountModel

/**
 * Converts AccountItem (domain model) to AccountEntity (database model).
 * @param userUid The ID of the currently logged-in user.
 */
fun AccountModel.toEntity(userUid: String): AccountEntity {
    return AccountEntity(
        id = this.id,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        balance = this.balance,
        userUid = userUid
    )
}

/**
 * Converts AccountEntity (database model) to AccountItem (domain model).
 */
fun AccountEntity.toDomain(): AccountModel {
    return AccountModel(
        id = this.id,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        balance = this.balance,
    )
}