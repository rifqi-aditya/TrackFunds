package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.transaction.TransactionRunner
import com.rifqi.trackfunds.core.domain.transaction.model.UpdateTransactionParams
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import java.time.LocalTime
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UpdateTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val sessionRepository: UserSessionRepository,
    private val transactionRunner: TransactionRunner
) : UpdateTransactionUseCase {

    override suspend operator fun invoke(params: UpdateTransactionParams): Result<Unit> = try {
        val userUid = sessionRepository.requireActiveUserId()

        transactionRunner {
            val original = transactionRepository
                .findTransactionWithDetailsById(params.id, userUid)
                ?: throw IllegalStateException("Transaction not found")

            val updated = original.copy(
                description = params.description,
                amount = params.amount,
                type = params.type,
                date = params.date.atTime(LocalTime.now()),
                account = params.account,
                category = params.category ?: original.category,
                savingsGoal = params.savingsGoal ?: original.savingsGoal,
                receiptImageUri = params.receiptImageUri ?: original.receiptImageUri,
                items = params.items
            )

            val oldAccount = accountRepository.getAccountById(original.account.id).getOrThrow()
            val reverted = oldAccount.revertTransaction(original)
            accountRepository.saveAccount(reverted)

            val baseForApply =
                if (updated.account.id == original.account.id) reverted
                else accountRepository.getAccountById(updated.account.id).getOrThrow()

            val finalAccount = baseForApply.applyTransaction(updated)
            accountRepository.saveAccount(finalAccount)

            transactionRepository.saveTransaction(updated, userUid)
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