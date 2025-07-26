package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountsUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository
) : GetAccountsUseCase {

    override operator fun invoke(): Flow<List<AccountModel>> {
        return accountRepository.getAllAccounts()
    }
}