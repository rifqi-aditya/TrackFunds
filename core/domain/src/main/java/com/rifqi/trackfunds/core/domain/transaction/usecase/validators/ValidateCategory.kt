package com.rifqi.trackfunds.core.domain.transaction.usecase.validators

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject

/**
 * Validates that a category is selected for any transaction.
 * Returns a failure result if the category is null.
 */
class ValidateCategory @Inject constructor() {
    operator fun invoke(category: Category?): ValidationResult {
        if (category == null) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "Category must be selected."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}