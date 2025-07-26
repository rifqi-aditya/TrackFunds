package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountByIdUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : GetAccountByIdUseCase {
    override suspend operator fun invoke(accountId: String): Result<AccountModel> {
        return repository.getAccountById(accountId)
    }
}