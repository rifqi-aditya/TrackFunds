package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val savingsGoalRepository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val transactionRunner: AppTransactionRunner
) : DeleteTransactionUseCase {

    override suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: return Result.failure(IllegalStateException("User UID is not set"))

        return try {
            // Bungkus semua operasi dalam satu transaksi atomik
            transactionRunner {
                // 1. Dapatkan akun terkait
                val account = accountRepository.getAccountById(transaction.account.id).getOrThrow()

                // 2. Hitung saldo yang sudah dikembalikan (reverted)
                val revertedBalance = if (transaction.type == TransactionType.INCOME) {
                    account.balance.subtract(transaction.amount)
                } else {
                    account.balance.add(transaction.amount)
                }

                // 3. Update saldo akun
                accountRepository.updateAccount(account.copy(balance = revertedBalance))
                    .getOrThrow()

                // 4. Update tujuan tabungan jika ada
                transaction.savingsGoal?.let {
                    if (transaction.type == TransactionType.EXPENSE) {
                        savingsGoalRepository.addFunds(userUid,it.id, transaction.amount.negate())
                            .getOrThrow()
                    }
                }

                // 5. Terakhir, hapus transaksi itu sendiri
                transactionRepository.deleteTransaction(transaction.id).getOrThrow()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}