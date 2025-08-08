package com.rifqi.trackfunds.core.domain.auth.usecase

import com.rifqi.trackfunds.core.domain.auth.model.LoginParams
import com.rifqi.trackfunds.core.domain.auth.repository.AuthRepository
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateEmail
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail
) : LoginUseCase {
    override suspend operator fun invoke(params: LoginParams): Result<Unit> {
        val emailResult = validateEmail(params.email)
        if (!emailResult.isSuccess) {
            return Result.failure(IllegalArgumentException(emailResult.errorMessage))
        }
        if (params.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty."))
        }

        return authRepository.login(params.email, params.password)
    }
}