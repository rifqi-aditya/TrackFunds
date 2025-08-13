package com.rifqi.trackfunds.core.domain.account.usecase

import kotlinx.coroutines.flow.Flow

interface ObserveAccountCountUseCase {
    operator fun invoke(): Flow<Int>
}