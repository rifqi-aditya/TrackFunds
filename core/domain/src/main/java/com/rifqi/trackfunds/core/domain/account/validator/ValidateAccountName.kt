package com.rifqi.trackfunds.core.domain.account.validator

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

class ValidateAccountName @Inject constructor() {
    operator fun invoke(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "Account name cannot be empty.")
        }
        return ValidationResult(true)
    }
}