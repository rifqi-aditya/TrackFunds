package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : UpdateAccountUseCase {
    override suspend operator fun invoke(account: AccountItem) {
        repository.updateAccount(account)
    }
}