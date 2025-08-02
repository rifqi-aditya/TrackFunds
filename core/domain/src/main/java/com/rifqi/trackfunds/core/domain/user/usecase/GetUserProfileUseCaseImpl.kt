package com.rifqi.trackfunds.core.domain.user.usecase

import com.rifqi.trackfunds.core.domain.common.model.User
import com.rifqi.trackfunds.core.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserProfileUseCase {
    override operator fun invoke(): Flow<User?> {
        return userRepository.getProfile()
    }
}