package com.rifqi.trackfunds.core.domain.user.usecase

import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateFullName
import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import javax.inject.Inject


data class ProfileValidationResult(
    val fullNameError: String? = null
) {
    val isSuccess: Boolean get() = fullNameError == null
}

class ValidateFullName @Inject constructor() {
    operator fun invoke(fullName: String): ValidationResult {
        if (fullName.isBlank()) {
            return ValidationResult(false, "Full name cannot be empty.")
        }
        return ValidationResult(true)
    }
}

class ValidateProfileUseCase @Inject constructor(
    private val validateFullName: ValidateFullName
) {
    operator fun invoke(fullName: String): ProfileValidationResult {
        val fullNameResult = validateFullName(fullName)

        if (!fullNameResult.isSuccess) {
            return ProfileValidationResult(fullNameError = fullNameResult.errorMessage)
        }
        return ProfileValidationResult()
    }
}