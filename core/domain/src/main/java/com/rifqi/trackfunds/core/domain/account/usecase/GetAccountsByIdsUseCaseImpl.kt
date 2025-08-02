package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import javax.inject.Inject

class GetAccountsByIdsUseCaseImpl @Inject constructor(private val repository: AccountRepository) :
    GetAccountsByIdsUseCase {
    override suspend operator fun invoke(ids: List<String>): List<Account> {
        return repository.getAccountsByIds(ids)
    }
}