package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class AddAccountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : AddAccountUseCase {
    override suspend operator fun invoke(account: AccountModel) {
        repository.addAccount(account)
    }
}