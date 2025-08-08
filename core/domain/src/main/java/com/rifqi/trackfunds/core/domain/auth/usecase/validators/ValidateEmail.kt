package com.rifqi.trackfunds.core.domain.auth.usecase.validators

import android.util.Patterns
import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

/**
 * Use case to validate an email address.
 * It follows a single responsibility principle.
 */
class ValidateEmail @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(false, "Email cannot be empty.")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(false, "Please enter a valid email address.")
        }
        return ValidationResult(true)
    }
}