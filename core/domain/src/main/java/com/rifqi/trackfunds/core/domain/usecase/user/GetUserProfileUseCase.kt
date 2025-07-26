package com.rifqi.trackfunds.core.domain.usecase.user

import com.rifqi.trackfunds.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface GetUserProfileUseCase {
    operator fun invoke(): Flow<UserModel?>
}