package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import java.math.BigDecimal
import javax.inject.Inject

class UpdateTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val transactionRunner: AppTransactionRunner
) : UpdateTransactionUseCase {

    override suspend operator fun invoke(
        transaction: Transaction,
        oldAmount: BigDecimal,
        oldAccountId: String
    ): Result<Unit> {
        return try {
            transactionRunner {
                if (transaction.account.id == oldAccountId) {
                    // --- KASUS 1: AKUN TETAP SAMA ---
                    val account =
                        accountRepository.getAccountById(transaction.account.id).getOrThrow()
                    val oldEffect =
                        if (transaction.type == TransactionType.INCOME) oldAmount else oldAmount.negate()
                    val newEffect =
                        if (transaction.type == TransactionType.INCOME) transaction.amount else transaction.amount.negate()
                    val difference = newEffect - oldEffect
                    val finalBalance = account.balance.add(difference)
                    accountRepository.updateAccount(account.copy(balance = finalBalance))
                        .getOrThrow()
                } else {
                    // --- KASUS 2: AKUN BERUBAH ---
                    // 1. Kembalikan saldo di AKUN LAMA
                    val oldAccount = accountRepository.getAccountById(oldAccountId).getOrThrow()
                    val revertedBalance = if (transaction.type == TransactionType.INCOME) {
                        oldAccount.balance.subtract(oldAmount)
                    } else {
                        oldAccount.balance.add(oldAmount)
                    }
                    accountRepository.updateAccount(oldAccount.copy(balance = revertedBalance))
                        .getOrThrow()

                    // 2. Terapkan saldo di AKUN BARU
                    val newAccount =
                        accountRepository.getAccountById(transaction.account.id).getOrThrow()
                    val newBalance = if (transaction.type == TransactionType.INCOME) {
                        newAccount.balance.add(transaction.amount)
                    } else {
                        newAccount.balance.subtract(transaction.amount)
                    }
                    accountRepository.updateAccount(newAccount.copy(balance = newBalance))
                        .getOrThrow()
                }

                // Terakhir, update data transaksi itu sendiri
                transactionRepository.updateTransaction(transaction).getOrThrow()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}