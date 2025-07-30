package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.Account
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountsByIdsUseCaseImpl @Inject constructor(private val repository: AccountRepository) :
    GetAccountsByIdsUseCase {
    override suspend operator fun invoke(ids: List<String>): List<Account> {
        return repository.getAccountsByIds(ids)
    }
}