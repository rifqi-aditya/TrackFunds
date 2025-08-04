package com.rifqi.trackfunds.core.domain.auth.usecase

import com.rifqi.trackfunds.core.domain.common.model.User
import com.rifqi.trackfunds.core.domain.auth.repository.AuthRepository
import com.rifqi.trackfunds.core.domain.user.repository.UserRepository
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : RegisterUseCase {

    override suspend operator fun invoke(
        email: String,
        pass: String,
        fullName: String
    ): Result<Unit> {
        return authRepository.register(email, pass).map { uid ->
            val newUser = User(uid = uid, fullName = fullName, email = email)
            userRepository.createOrUpdateProfile(newUser, null).getOrThrow()
        }
    }
}