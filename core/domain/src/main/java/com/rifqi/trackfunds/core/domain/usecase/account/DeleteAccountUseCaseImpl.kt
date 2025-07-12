package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : DeleteAccountUseCase {
    override suspend operator fun invoke(accountId: String) {
        repository.deleteAccount(accountId)
    }
}