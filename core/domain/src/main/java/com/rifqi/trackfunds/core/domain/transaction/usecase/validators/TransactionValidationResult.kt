package com.rifqi.trackfunds.core.domain.transaction.usecase.validators

data class TransactionValidationResult(
    val amountError: String? = null,
    val accountError: String? = null,
) {
    val isSuccess: Boolean
        get() = amountError == null && accountError == null
}