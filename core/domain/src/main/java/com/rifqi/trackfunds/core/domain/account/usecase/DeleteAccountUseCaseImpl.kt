package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository,
) : DeleteAccountUseCase {

    override suspend operator fun invoke(accountId: String): Result<Unit> {
        return runCatching {
            accountRepository.deleteAccount(accountId)
        }
    }
}