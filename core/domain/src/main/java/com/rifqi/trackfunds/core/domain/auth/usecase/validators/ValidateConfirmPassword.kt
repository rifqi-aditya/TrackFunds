package com.rifqi.trackfunds.core.domain.auth.usecase.validators

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

/**
 * Use case to validate that the password and confirmation password match.
 */
class ValidateConfirmPassword @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(false, "Passwords do not match.")
        }
        return ValidationResult(true)
    }
}