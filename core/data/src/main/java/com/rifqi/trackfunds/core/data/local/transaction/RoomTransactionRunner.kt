package com.rifqi.trackfunds.core.data.local.transaction

import androidx.room.withTransaction
import com.rifqi.trackfunds.core.data.local.AppDatabase
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import javax.inject.Inject

class RoomTransactionRunner @Inject constructor(
    private val db: AppDatabase
) : AppTransactionRunner {

    override suspend operator fun <T> invoke(block: suspend () -> T): T {
        return db.withTransaction(block)
    }
}