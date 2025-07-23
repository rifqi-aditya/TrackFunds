package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import javax.inject.Inject

class PerformTransferUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val transactionRunner: AppTransactionRunner // Inject Transaction Runner
) : PerformTransferUseCase {

    override suspend operator fun invoke(expense: TransactionItem, income: TransactionItem): Result<Unit> {
        // Gunakan try-catch untuk menangani semua kemungkinan error
        return try {
            // Bungkus semua operasi dalam satu transaksi atomik
            transactionRunner {
                // 1. Dapatkan akun asal dan tujuan dari repository
                val fromAccount = accountRepository.getAccountById(expense.account.id).getOrThrow()
                val toAccount = accountRepository.getAccountById(income.account.id).getOrThrow()

                // 2. Hitung saldo baru untuk kedua akun
                val newFromBalance = fromAccount.balance.subtract(expense.amount)
                val newToBalance = toAccount.balance.add(income.amount)

                // 3. Masukkan dua transaksi baru (keluar dan masuk)
                transactionRepository.insertTransaction(expense).getOrThrow()
                transactionRepository.insertTransaction(income).getOrThrow()

                // 4. Perbarui kedua akun dengan saldo baru
                accountRepository.updateAccount(fromAccount.copy(balance = newFromBalance)).getOrThrow()
                accountRepository.updateAccount(toAccount.copy(balance = newToBalance)).getOrThrow()
            }
            // Jika semua di dalam 'transactionRunner' berhasil, kembalikan sukses
            Result.success(Unit)
        } catch (e: Exception) {
            // Jika ada error di mana pun, kembalikan gagal
            Result.failure(e)
        }
    }
}