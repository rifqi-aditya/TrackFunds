package com.rifqi.trackfunds.core.domain.usecase.user

import com.rifqi.trackfunds.core.domain.model.User
import com.rifqi.trackfunds.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserProfileUseCase {
    override operator fun invoke(): Flow<User?> {
        return userRepository.getProfile()
    }
}