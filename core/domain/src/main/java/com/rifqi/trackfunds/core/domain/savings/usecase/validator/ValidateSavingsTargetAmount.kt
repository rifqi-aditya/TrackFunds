package com.rifqi.trackfunds.core.domain.savings.usecase.validator

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Use case to validate the target amount of a savings goal.
 */
class ValidateSavingsTargetAmount @Inject constructor() {
    operator fun invoke(amountString: String): ValidationResult {
        if (amountString.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Target amount can't be empty."
            )
        }
        val amount = amountString.toBigDecimalOrNull()
            ?: return ValidationResult(
                isSuccess = false,
                errorMessage = "Invalid number format."
            )

        if (amount < BigDecimal("10000")) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Target must be at least IDR 10,000."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}