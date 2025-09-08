package com.rifqi.trackfunds.core.data.local.db

import androidx.room.withTransaction
import com.rifqi.trackfunds.core.domain.transaction.TransactionRunner
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RoomTransactionRunner @Inject constructor(
    private val db: TrackFundsDatabase
) : TransactionRunner {
    override suspend fun <T> invoke(block: suspend () -> T): T = db.withTransaction { block() }
}