package com.rifqi.trackfunds.core.domain.auth.usecase.validators

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

/**
 * Use case to comprehensively validate a full name.
 */
class ValidateFullName @Inject constructor(){
    operator fun invoke(fullName: String): ValidationResult {
        // 1. Check if blank
        if (fullName.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Full name can't be empty."
            )
        }

        // 2. Check for minimum length
        if (fullName.length < 3) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Full name must be at least 3 characters."
            )
        }

        // 3. Check for valid characters (only letters, spaces, hyphens, apostrophes)
        val namePattern = Regex("^[a-zA-Z'\\- ]+\$")
        if (!fullName.matches(namePattern)) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Name can only contain letters, spaces, hyphens, and apostrophes."
            )
        }

        // If all checks pass, return success
        return ValidationResult(isSuccess = true)
    }
}