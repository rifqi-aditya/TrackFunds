package com.rifqi.trackfunds.core.domain.account.validator

data class AccountValidationResult(
    val nameError: String? = null,
    val balanceError: String? = null,
    val iconError: String? = null
) {
    val isSuccess: Boolean
        get() = nameError == null && balanceError == null && iconError == null
}