package com.rifqi.trackfunds.core.domain.savings.usecase.validator

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

/**
 * Use case to validate that an icon has been selected.
 */
class ValidateIcon @Inject constructor() {
    operator fun invoke(iconIdentifier: String): ValidationResult {
        if (iconIdentifier.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Please choose an icon."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}