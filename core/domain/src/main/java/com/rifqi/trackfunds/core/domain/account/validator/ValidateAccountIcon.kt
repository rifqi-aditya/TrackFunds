package com.rifqi.trackfunds.core.domain.account.validator

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

class ValidateAccountIcon @Inject constructor() {
    operator fun invoke(iconIdentifier: String): ValidationResult {
        if (iconIdentifier.isBlank()) {
            return ValidationResult(false, "An icon must be selected.")
        }
        return ValidationResult(true)
    }
}