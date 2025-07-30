package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.Account
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountByIdUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : GetAccountByIdUseCase {
    override suspend operator fun invoke(accountId: String): Result<Account> {
        return repository.getAccountById(accountId)
    }
}