package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAccountCountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository,
) : ObserveAccountCountUseCase {
    override fun invoke(): Flow<Int> {
        return repository.observeAccountCount()
    }
}