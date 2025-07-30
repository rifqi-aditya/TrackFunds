package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.Account
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class AddAccountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : AddAccountUseCase {
    override suspend operator fun invoke(account: Account) {
        repository.addAccount(account)
    }
}