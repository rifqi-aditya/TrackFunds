package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal

data class Account(
    /**
     * Unique identifier for the account.
     */
    val id: String,
    /**
     * Name of the account.
     */
    val name: String,
    /**
     * Optional identifier for the account's icon.
     */
    val iconIdentifier: String?,
    /**
     * Current balance of the account.
     */
    val balance: BigDecimal
) {
    /**
     * Applies a transaction to the account and returns a new Account object with the updated balance.
     *
     * @param transaction The transaction to apply.
     * @return A new Account object with the updated balance.
     */
    fun applyTransaction(transaction: Transaction): Account {
        val newBalance = when (transaction.type) {
            TransactionType.INCOME -> this.balance.add(transaction.amount)
            TransactionType.EXPENSE, TransactionType.SAVINGS -> this.balance.subtract(transaction.amount)
        }
        return this.copy(balance = newBalance)
    }

    /**
     * Menghitung dan mengembalikan objek Account baru dengan saldo yang
     * sudah dikembalikan (dibatalkan) dari sebuah transaksi.
     */
    fun revertTransaction(transaction: Transaction): Account {
        val revertedBalance = when (transaction.type) {
            TransactionType.INCOME -> this.balance.subtract(transaction.amount)
            TransactionType.EXPENSE, TransactionType.SAVINGS -> this.balance.add(transaction.amount)
        }
        return this.copy(balance = revertedBalance)
    }
}