package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.validator.AccountValidationResult
import com.rifqi.trackfunds.core.domain.account.validator.ValidateAccountIcon
import com.rifqi.trackfunds.core.domain.account.validator.ValidateAccountName
import com.rifqi.trackfunds.core.domain.account.validator.ValidateInitialBalance
import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

class ValidateAccountUseCase @Inject constructor(
    private val validateName: ValidateAccountName,
    private val validateBalance: ValidateInitialBalance,
    private val validateIcon: ValidateAccountIcon
) {
    operator fun invoke(
        name: String,
        balance: String,
        icon: String,
        isEditMode: Boolean
    ): AccountValidationResult {
        val nameResult = validateName(name)
        val iconResult = validateIcon(icon)

        val balanceResult = if (!isEditMode) {
            validateBalance(balance)
        } else {
            ValidationResult(true)
        }

        if (!nameResult.isSuccess || !balanceResult.isSuccess || !iconResult.isSuccess) {
            return AccountValidationResult(
                nameError = nameResult.errorMessage,
                balanceError = balanceResult.errorMessage,
                iconError = iconResult.errorMessage
            )
        }

        return AccountValidationResult()
    }
}