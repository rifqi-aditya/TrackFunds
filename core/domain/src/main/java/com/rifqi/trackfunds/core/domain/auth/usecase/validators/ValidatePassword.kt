package com.rifqi.trackfunds.core.domain.auth.usecase.validators

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

/**
 * Use case to validate a password.
 */
class ValidatePassword @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Password can't be empty."
            )
        }
        if (password.length < 6) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Password must be at least 6 characters."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}