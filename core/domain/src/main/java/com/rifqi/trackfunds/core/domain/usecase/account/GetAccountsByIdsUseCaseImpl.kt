package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountsByIdsUseCaseImpl @Inject constructor(private val repository: AccountRepository) :
    GetAccountsByIdsUseCase {
    override suspend operator fun invoke(ids: List<String>): List<AccountModel> {
        return repository.getAccountsByIds(ids)
    }
}