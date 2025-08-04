package com.rifqi.trackfunds.core.domain.account.validator

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

class ValidateInitialBalance @Inject constructor() {
    operator fun invoke(balance: String): ValidationResult {
        if (balance.isBlank()) {
            return ValidationResult(false, "Initial balance cannot be empty.")
        }
        if (balance.toBigDecimalOrNull() == null) {
            return ValidationResult(false, "Please enter a valid number.")
        }
        return ValidationResult(true)
    }
}