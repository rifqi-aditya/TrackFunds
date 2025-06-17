package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.util.UUID

data class AccountItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconIdentifier: String?,
    val balance: BigDecimal
)