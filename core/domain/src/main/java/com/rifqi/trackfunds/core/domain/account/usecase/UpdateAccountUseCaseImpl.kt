package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : UpdateAccountUseCase {
    override suspend operator fun invoke(account: Account) {
        repository.updateAccount(account)
    }
}