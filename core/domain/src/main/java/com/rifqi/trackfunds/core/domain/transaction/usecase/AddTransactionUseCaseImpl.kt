package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.transaction.TransactionRunner
import com.rifqi.trackfunds.core.domain.transaction.model.AddTransactionParams
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AddTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val sessionRepository: UserSessionRepository,
    private val transactionRunner: TransactionRunner
) : AddTransactionUseCase {

    override suspend operator fun invoke(params: AddTransactionParams): Result<Unit> = try {
        val userUid = sessionRepository.requireActiveUserId()

        transactionRunner {
            val newTransaction = Transaction(
                id = UUID.randomUUID().toString(),
                description = params.description,
                amount = params.amount,
                type = params.type,
                date = params.date.atTime(LocalTime.now()),
                account = params.account,
                category = params.category,
                savingsGoal = params.savingsGoal,
                receiptImageUri = params.receiptImageUri,
                items = params.items
            )

            val originalAccount = accountRepository
                .getAccountById(newTransaction.account.id)
                .getOrThrow()

            val updatedAccount = originalAccount.applyTransaction(newTransaction)

            // Jika repositori butuh konteks user, teruskan uid di sini
            transactionRepository.saveTransaction(newTransaction, userUid)
            accountRepository.saveAccount(updatedAccount)
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