package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import kotlinx.coroutines.flow.Flow

interface GetAccountsUseCase {
    operator fun invoke(): Flow<List<Account>>
}