package com.rifqi.trackfunds.core.domain.common.model

/**
 * Represents the outcome of a validation attempt.
 *
 * @property isSuccess True if validation passed, false otherwise.
 * @property errorMessage An optional message describing the validation failure,
 *           present only if `isSuccess` is false.
 */

data class ValidationResult(
    val isSuccess: Boolean,
    val errorMessage: String? = null
)