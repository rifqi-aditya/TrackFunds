package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountByIdUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : GetAccountByIdUseCase {
    override suspend operator fun invoke(accountId: String): AccountItem? {
        return repository.getAccountById(accountId)
    }
}