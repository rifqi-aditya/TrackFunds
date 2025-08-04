package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.savings.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val savingsGoalRepository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val transactionRunner: AppTransactionRunner
) : DeleteTransactionUseCase {

    override suspend operator fun invoke(transactionId: String): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUid.first()
                ?: return Result.failure(IllegalStateException("User UID is not set"))

            transactionRunner {
                val transaction =
                    transactionRepository.findTransactionWithDetailsById(transactionId, userUid)
                        ?: throw IllegalStateException("Transaction not found for deletion")

                val account = accountRepository.getAccountById(transaction.account.id, userUid).getOrThrow()

                val revertedAccount = account.revertTransaction(transaction)
                accountRepository.saveAccount(revertedAccount, userUid)

                transaction.savingsGoal?.let {
                    if (transaction.type == TransactionType.EXPENSE) {
                        savingsGoalRepository.addFunds(userUid, it.id, transaction.amount.negate())
                    }
                }

                // 5. Terakhir, hapus transaksi itu sendiri
                transactionRepository.deleteTransaction(transaction.id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}