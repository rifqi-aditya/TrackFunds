package com.rifqi.trackfunds.core.domain.transaction.usecase.validators

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import java.math.BigDecimal
import javax.inject.Inject

class ValidateAmount @Inject constructor() {
    operator fun invoke(amountString: String): ValidationResult {
        if (amountString.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Amount can't be empty."
            )
        }
        if (amountString.toBigDecimalOrNull() == null) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Please enter a valid number."
            )
        }
        if (amountString.toBigDecimal() <= BigDecimal.ZERO) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Amount must be greater than zero."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}