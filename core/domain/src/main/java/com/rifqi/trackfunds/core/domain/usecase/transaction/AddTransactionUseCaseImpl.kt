package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository // Diperlukan untuk update saldo
) : AddTransactionUseCase {

    override suspend operator fun invoke(transaction: Transaction) {
        // Di sini bisa ditambahkan validasi bisnis, misalnya:
//         if (transaction.amount <= BigDecimal.ZERO) throw InvalidAmountException("Amount must be positive.")

        // Logika utama: simpan transaksi dan update saldo akun
        transactionRepository.insertTransaction(transaction)

        // Logika untuk mengupdate saldo akan ditangani di dalam implementasi repository
        // atau bisa juga dihandle di sini jika lebih kompleks.
    }
}