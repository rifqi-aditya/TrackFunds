package com.rifqi.trackfunds.core.domain.usecase.validator

import android.util.Patterns
import com.rifqi.trackfunds.core.domain.model.ValidationResult
import javax.inject.Inject

/**
 * Use case to validate an email address.
 * It follows a single responsibility principle.
 */
class ValidateEmail @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Email can't be empty."
            )
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Invalid email format."
            )
        }

        return ValidationResult(isSuccess = true)
    }
}