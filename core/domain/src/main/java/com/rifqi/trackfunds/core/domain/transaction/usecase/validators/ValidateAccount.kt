package com.rifqi.trackfunds.core.domain.transaction.usecase.validators

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

class ValidateAccount @Inject constructor() {
    operator fun invoke(account: Account?): ValidationResult {
        if (account == null) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Account must be selected."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}