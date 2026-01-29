package com.rifqi.trackfunds.core.ui.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.ui.R

@StringRes
fun TransactionType.displayLabelRes(): Int = when (this) {
    TransactionType.EXPENSE -> R.string.transaction_type_expense
    TransactionType.INCOME  -> R.string.transaction_type_income
    TransactionType.SAVINGS -> R.string.transaction_type_savings
}

/** Untuk Compose */
@Composable
fun TransactionType.displayLabel(): String = stringResource(displayLabelRes())

/** Untuk non-Compose (mis. ViewModel/Service) */
fun TransactionType.displayLabel(resources: android.content.res.Resources): String =
    resources.getString(displayLabelRes())

