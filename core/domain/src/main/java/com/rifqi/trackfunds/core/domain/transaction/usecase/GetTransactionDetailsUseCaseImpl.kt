package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetTransactionDetailsUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository,
    private val sessionRepository: UserSessionRepository
) : GetTransactionDetailsUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(transactionId: String): Flow<Transaction?> {
        return sessionRepository.userUidFlow()
            .flatMapLatest { uid ->
                if (uid.isNullOrBlank()) {
                    flowOf(null)
                } else {
                    repository.getTransactionWithDetails(
                        transactionId = transactionId,
                        userUid = uid
                    )
                }
            }
            .distinctUntilChanged()
    }
}