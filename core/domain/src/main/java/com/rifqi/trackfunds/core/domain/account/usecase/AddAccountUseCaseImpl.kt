package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import javax.inject.Inject

class AddAccountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : AddAccountUseCase {
    override suspend operator fun invoke(account: Account) {
        repository.addAccount(account)
    }
}