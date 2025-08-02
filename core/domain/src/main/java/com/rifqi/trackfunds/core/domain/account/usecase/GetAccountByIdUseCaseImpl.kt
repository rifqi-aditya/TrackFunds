package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import javax.inject.Inject

class GetAccountByIdUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : GetAccountByIdUseCase {
    override suspend operator fun invoke(accountId: String): Result<Account> {
        return repository.getAccountById(accountId)
    }
}