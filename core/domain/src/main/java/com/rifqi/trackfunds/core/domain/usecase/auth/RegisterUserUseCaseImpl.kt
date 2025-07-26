package com.rifqi.trackfunds.core.domain.usecase.auth

import com.rifqi.trackfunds.core.domain.model.UserModel
import com.rifqi.trackfunds.core.domain.repository.AuthRepository
import com.rifqi.trackfunds.core.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : RegisterUserUseCase {

    override suspend operator fun invoke(
        email: String,
        pass: String,
        fullName: String
    ): Result<Unit> {
        return authRepository.register(email, pass).map { uid ->
            val newUserModel = UserModel(uid = uid, fullName = fullName, email = email)
            userRepository.createOrUpdateProfile(newUserModel, null).getOrThrow()
        }
    }
}