package com.rifqi.trackfunds.core.domain.account.model

import java.math.BigDecimal

data class AddAccountParams(
    val name: String,
    val initialBalance: BigDecimal,
    val iconIdentifier: String
)