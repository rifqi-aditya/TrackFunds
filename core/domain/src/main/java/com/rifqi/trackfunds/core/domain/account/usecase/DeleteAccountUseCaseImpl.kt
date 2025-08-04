package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteAccountUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : DeleteAccountUseCase {

    override suspend operator fun invoke(accountId: String): Result<Unit> {
        return runCatching {
            val userUid = userPreferencesRepository.userUid.first()
                ?: throw IllegalStateException("User not logged in.")

            accountRepository.deleteAccount(accountId, userUid)
        }
    }
}