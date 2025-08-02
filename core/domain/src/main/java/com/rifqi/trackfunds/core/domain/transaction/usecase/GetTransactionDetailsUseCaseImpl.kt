package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetTransactionDetailsUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : GetTransactionDetailsUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(transactionId: String): Flow<Transaction?> {
        return userPreferencesRepository.userUid.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(null)
            } else {
                repository.getTransactionWithDetails(
                    transactionId = transactionId,
                    userUid = userUid
                )
            }
        }
    }
}