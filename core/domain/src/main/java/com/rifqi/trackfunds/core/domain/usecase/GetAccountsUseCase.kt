package com.rifqi.trackfunds.core.domain.usecase

import com.rifqi.trackfunds.core.domain.model.AccountItem
import kotlinx.coroutines.flow.Flow

interface GetAccountsUseCase {
    operator fun invoke(): Flow<List<AccountItem>>
}