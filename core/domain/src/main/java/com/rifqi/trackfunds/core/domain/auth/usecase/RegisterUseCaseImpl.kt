package com.rifqi.trackfunds.core.domain.auth.usecase

import com.rifqi.trackfunds.core.domain.auth.model.RegisterParams
import com.rifqi.trackfunds.core.domain.auth.repository.AuthRepository
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateConfirmPassword
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateEmail
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateFullName
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidatePassword
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword,
    private val validateFullName: ValidateFullName,
) : RegisterUseCase {
    override suspend operator fun invoke(params: RegisterParams): Result<String> {
        // Jalankan semua validasi terlebih dahulu
        val emailResult = validateEmail(params.email)
        val passwordResult = validatePassword(params.password)
        val confirmPasswordResult = validateConfirmPassword(params.password, params.confirmPassword)
        val fullNameResult = validateFullName(params.fullName)

        // Jika ada satu saja yang gagal, kembalikan error
        if (!emailResult.isSuccess) return Result.failure(IllegalArgumentException(emailResult.errorMessage))
        if (!passwordResult.isSuccess) return Result.failure(IllegalArgumentException(passwordResult.errorMessage))
        if (!confirmPasswordResult.isSuccess) return Result.failure(
            IllegalArgumentException(
                confirmPasswordResult.errorMessage
            )
        )
        if (!fullNameResult.isSuccess) return Result.failure(IllegalArgumentException(fullNameResult.errorMessage))

        // Jika semua validasi lolos, panggil repository
        return authRepository.register(params.email, params.password, params.fullName)
    }
}