package com.rifqi.trackfunds.core.domain.user.usecase

import com.rifqi.trackfunds.core.domain.common.model.User
import kotlinx.coroutines.flow.Flow

interface GetUserUseCase {
    operator fun invoke(): Flow<User?>
}