package com.rifqi.trackfunds.core.domain.validator.auth

import com.rifqi.trackfunds.core.domain.model.ValidationResult
import javax.inject.Inject

/**
 * Use case to validate that the password and confirmation password match.
 */
class ValidateConfirmPassword @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Passwords do not match."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}