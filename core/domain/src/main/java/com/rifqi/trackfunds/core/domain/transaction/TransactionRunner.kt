package com.rifqi.trackfunds.core.domain.transaction

/**
 * Interface to run multiple database operations in a single atomic transaction.
 */
interface TransactionRunner {
    suspend operator fun <T> invoke(block: suspend () -> T): T
}