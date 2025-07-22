package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import kotlinx.coroutines.flow.Flow

interface GetTransactionByIdUseCase {
    /**
     * @param transactionId ID dari transaksi yang ingin diambil.
     * @return Sebuah Flow yang akan meng-emit TransactionItem, atau null jika tidak ditemukan.
     */
    operator fun invoke(transactionId: String): Flow<TransactionItem?>
}