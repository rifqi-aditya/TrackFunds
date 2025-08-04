package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAccountByIdUseCaseImpl @Inject constructor(
    private val repository: AccountRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : GetAccountByIdUseCase {
    override suspend operator fun invoke(accountId: String): Result<Account> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: return Result.failure(IllegalStateException("User UID is not set."))

        return repository.getAccountById(accountId, userUid)
    }
}