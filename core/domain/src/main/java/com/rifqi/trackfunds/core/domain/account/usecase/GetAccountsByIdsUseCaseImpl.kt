package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAccountsByIdsUseCaseImpl @Inject constructor(
    private val repository: AccountRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) :
    GetAccountsByIdsUseCase {
    override suspend operator fun invoke(ids: List<String>): List<Account> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: throw IllegalStateException("User UID is not set")
        return repository.getAccountsByIds(ids, userUid)
    }
}