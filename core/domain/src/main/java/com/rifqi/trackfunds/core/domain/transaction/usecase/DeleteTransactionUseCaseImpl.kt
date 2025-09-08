package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.savings.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.transaction.TransactionRunner
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class DeleteTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val savingsGoalRepository: SavingsGoalRepository,
    private val sessionRepository: UserSessionRepository,
    private val transactionRunner: TransactionRunner
) : DeleteTransactionUseCase {

    override suspend operator fun invoke(transactionId: String): Result<Unit> = try {
        val userUid = sessionRepository.requireActiveUserId()

        transactionRunner {
            val tx = transactionRepository
                .findTransactionWithDetailsById(transactionId, userUid)
                ?: throw IllegalStateException("Transaction not found for deletion")

            val account = accountRepository.getAccountById(tx.account.id).getOrThrow()
            val revertedAccount = account.revertTransaction(tx)
            accountRepository.saveAccount(revertedAccount)

            tx.savingsGoal?.let { goal ->
                if (tx.type == TransactionType.EXPENSE) {
                    savingsGoalRepository.addFunds(userUid, goal.id, tx.amount.negate())
                }
            }

            transactionRepository.deleteTransaction(tx.id)
        }

        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}