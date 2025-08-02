package com.rifqi.trackfunds.core.domain.transaction.usecase.validators

import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import java.time.LocalDate
import javax.inject.Inject

/**
 * Validates that the provided date is selected and is not in the future.
 */
class ValidateDate @Inject constructor() {
    operator fun invoke(date: LocalDate?): ValidationResult {
        if (date == null) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Date must be selected."
            )
        }

        val today = LocalDate.now()

        if (date.isAfter(today)) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Date cannot be in the future."
            )
        }

        return ValidationResult(isSuccess = true)
    }
}