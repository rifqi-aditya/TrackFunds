package com.rifqi.trackfunds.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.UUID

@Parcelize
data class AccountItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconIdentifier: String?,
    val balance: BigDecimal
) : Parcelable