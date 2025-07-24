package com.rifqi.trackfunds.core.domain.validator.savings

import com.rifqi.trackfunds.core.domain.model.ValidationResult
import javax.inject.Inject

/**
 * Use case to validate the name of a savings goal.
 */
class ValidateSavingsGoalName @Inject constructor() {
    operator fun invoke(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Goal name can't be empty."
            )
        }
        if (name.length < 3) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Goal name must be at least 3 characters."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}